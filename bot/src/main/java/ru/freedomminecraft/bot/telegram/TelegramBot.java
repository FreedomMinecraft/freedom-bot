package ru.freedomminecraft.bot.telegram;

import java.util.Optional;

import org.telegram.telegrambots.longpolling.util.DefaultLongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import ru.freedomminecraft.bot.command.CommandDispatcher;

public class TelegramBot extends DefaultLongPollingUpdateConsumer{
    private final TelegramClient client;
    private final CommandDispatcher dispatcher;

    private void send(long chatId, String text) {
        SendMessage message = SendMessage.builder()
            .chatId(chatId)
            .text(text)
            .build();
        try {
            client.execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Не удалось отправить в чат " + chatId + ": " + e.getMessage());
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
