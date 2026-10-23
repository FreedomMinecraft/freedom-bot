package ru.freedomminecraft.bot.telegram;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.telegram.telegrambots.longpolling.util.DefaultLongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.api.methods.ParseMode;

import ru.freedomminecraft.bot.command.CommandDispatcher;

public class TelegramBot extends DefaultLongPollingUpdateConsumer{
    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    private final TelegramClient client;
    private final CommandDispatcher dispatcher;

    private void send(long chatId, String text) {
        SendMessage message = SendMessage.builder()
            .chatId(chatId)
            .text(text)
            .parseMode(ParseMode.HTML)
            .build();
        try {
            client.execute(message);
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
        String text = update.getMessage().getText();

        Optional<String> reply = dispatcher.dispatch(text);
        if (reply.isPresent()) {
            send(chatId, reply.get());
        }
    }
}
