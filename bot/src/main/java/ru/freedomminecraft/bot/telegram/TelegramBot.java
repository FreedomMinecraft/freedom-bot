package ru.freedomminecraft.bot.telegram;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.telegram.telegrambots.longpolling.util.DefaultLongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import ru.freedomminecraft.bot.callback.CallbackDispatcher;
import ru.freedomminecraft.bot.callback.CallbackResult;
import ru.freedomminecraft.bot.command.CommandDispatcher;
import ru.freedomminecraft.bot.news.NewsChannel;
import ru.freedomminecraft.bot.news.NewsPost;
import ru.freedomminecraft.bot.reply.Reply;

public class TelegramBot extends DefaultLongPollingUpdateConsumer{
    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    private final TelegramClient client;
    private final CommandDispatcher dispatcher;
    private final CallbackDispatcher callbacks;
    private final Optional<NewsChannel> news;

    public TelegramBot(TelegramClient client, CommandDispatcher dispatcher, CallbackDispatcher callbacks, Optional<NewsChannel> news) {
        this.client = client;
        this.dispatcher = dispatcher;
        this.callbacks = callbacks;
        this.news = news;
    }

    @Override
    public void consume(Update update) {
        if (update.hasChannelPost()) {
            receiveChannelPost(update);
            return;
        }

        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
            return;
        }

        if (!update.hasMessage() || !update.getMessage().hasText()){
            return;
        }

        long chatId = update.getMessage().getChatId();
        long userId = update.getMessage().getFrom().getId();
        String text = update.getMessage().getText();

        Optional<Reply> reply = dispatcher.dispatch(chatId, userId, text);
        if (reply.isPresent()) {
            execute(ReplyConverter.toSendMessage(chatId, reply.get()), chatId);
        }
    }

    private void handleCallback(CallbackQuery query) {
        MaybeInaccessibleMessage message = query.getMessage();
        if (message == null) {
            execute(ReplyConverter.toAnswer(query.getId(), CallbackResult.silent()), 0);
            return;
        }

        long chatId = message.getChatId();
        int messageId = message.getMessageId();
        long userId = query.getFrom().getId();

        CallbackResult result = callbacks.dispatch(chatId, userId, messageId, query.getData());
        execute(ReplyConverter.toAnswer(query.getId(), result), chatId);
        if (result.update().isPresent()) {
            execute(ReplyConverter.toEditMessage(chatId, messageId, result.update().get()), chatId);
        }
    }

    private void receiveChannelPost(Update update){
        if (news.isEmpty()){
            return;
        }
        long channelId = update.getChannelPost().getChatId();
        int messageId = update.getChannelPost().getMessageId();
        String text = update.getChannelPost().getText();
        if (text == null){
            text = update.getChannelPost().getCaption();
        }
        news.get().receive(new NewsPost(channelId, messageId, text));
    }

    private void execute(BotApiMethod<?> method, long chatId) {
        try {
            client.execute(method);
        } catch (TelegramApiException e) {
            log.error("Не удалось выполнить {} в чате {}", method.getMethod(), chatId, e);
        }
    }
}
