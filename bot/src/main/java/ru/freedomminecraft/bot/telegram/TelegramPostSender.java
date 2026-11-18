package ru.freedomminecraft.bot.telegram;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.objects.ResponseParameters;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import ru.freedomminecraft.bot.broadcast.Delivery;
import ru.freedomminecraft.bot.broadcast.PostSender;
import ru.freedomminecraft.bot.news.NewsPost;

public class TelegramPostSender implements PostSender {
    private static final Logger log = LoggerFactory.getLogger(TelegramPostSender.class);

    private static final int FORBIDDEN = 403;
    private static final int TOO_MANY_REQUESTS = 429;

    private final TelegramClient client;

    public TelegramPostSender(TelegramClient client){
        this.client = client;
    }

    @Override
    public Delivery send(long userId, NewsPost post) {
        CopyMessage copy = CopyMessage.builder()
            .chatId(userId)
            .fromChatId(post.channelId())
            .messageId(post.messageId())
            .build();
        try {
            client.execute(copy);
            return Delivery.SENT;
        } catch (TelegramApiRequestException e){
            if (codeOf(e) == TOO_MANY_REQUESTS){
                waitBeforeRetry(e);
                return sendAgain(copy, userId);
            }
            return failure(e, userId);
        } catch (TelegramApiException e){
            return failure(e, userId);
        }
    }

    private Delivery sendAgain(CopyMessage copy, long userId){
        try{
            client.execute(copy);
            return Delivery.SENT;
        } catch (TelegramApiException e) {
            return failure(e, userId);
        }
    }

    private Delivery failure(TelegramApiException e, long userId){
        if (e instanceof TelegramApiRequestException request && codeOf(request) == FORBIDDEN){
            return Delivery.BLOCKED;
        }
        log.warn("Не удалось отправить пост пользователю {}: {}", userId, e.getMessage());
        return Delivery.FAILED;
    }

    private static int codeOf(TelegramApiRequestException e) {
        return e.getErrorCode() == null ? 0 : e.getErrorCode();
    }

    private static void waitBeforeRetry(TelegramApiRequestException e) {
        ResponseParameters parameters = e.getParameters();
        int seconds = 1;
        if (parameters != null && parameters.getRetryAfter() != null) {
            seconds = parameters.getRetryAfter();
        }
        log.warn("Telegram просит подождать {} с перед следующей отправкой", seconds);
        try {
            Thread.sleep(Duration.ofSeconds(seconds));
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
        }
    }
}
