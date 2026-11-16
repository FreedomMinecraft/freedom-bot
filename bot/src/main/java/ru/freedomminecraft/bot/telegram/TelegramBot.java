package ru.freedomminecraft.bot.telegram;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.telegram.telegrambots.longpolling.util.DefaultLongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import ru.freedomminecraft.bot.command.CommandDispatcher;
import ru.freedomminecraft.bot.reply.Reply;

public class TelegramBot extends DefaultLongPollingUpdateConsumer{
    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    private final TelegramClient client;
    private final CommandDispatcher dispatcher;

    private void send(long chatId, Reply reply) {
        try {
            client.execute(ReplyConverter.toSendMessage(chatId, reply));
        } catch (TelegramApiException e) {
            log.error("Не удалось отправить ответ в чат {}", chatId, e);
        }
    }

    public TelegramBot(TelegramClient client, CommandDispatcher dispatcher) {
        this.client = client;
        this.dispatcher = dispatcher;
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        long chatId = update.getMessage().getChatId();
        long userId = update.getMessage().getFrom().getId();
        String text = update.getMessage().getText();

        Optional<Reply> reply = dispatcher.dispatch(chatId, userId, text);
        if (reply.isPresent()) {
            send(chatId, reply.get());
        }
    }
}
