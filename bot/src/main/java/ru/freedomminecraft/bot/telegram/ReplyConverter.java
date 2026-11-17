package ru.freedomminecraft.bot.telegram;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.LinkPreviewOptions;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import ru.freedomminecraft.bot.callback.CallbackResult;
import ru.freedomminecraft.bot.reply.InlineButton;
import ru.freedomminecraft.bot.reply.InlineKeyboard;
import ru.freedomminecraft.bot.reply.Reply;

public final class ReplyConverter {

    private ReplyConverter() {};

    public static SendMessage toSendMessage(long chatId, Reply reply) {
        return SendMessage.builder()
            .chatId(chatId)
            .text(reply.text())
            .parseMode(ParseMode.HTML)
            .linkPreviewOptions(LinkPreviewOptions.builder().isDisabled(true).build())
            .replyMarkup(reply.keyboard().map(ReplyConverter::toMarkup).orElse(null))
            .build();
    }

    public static EditMessageText toEditMessage(long chatId, int messageId, Reply reply) {
        return EditMessageText.builder()
            .chatId(chatId)
            .messageId(messageId)
            .text(reply.text())
            .parseMode(ParseMode.HTML)
            .linkPreviewOptions(LinkPreviewOptions.builder().isDisabled(true).build())
            .replyMarkup(reply.keyboard().map(ReplyConverter::toMarkup).orElse(null))
            .build();
    }

    public static AnswerCallbackQuery toAnswer(String callbackQueryId, CallbackResult result) {
        return AnswerCallbackQuery.builder()
            .callbackQueryId(callbackQueryId)
            .text(result.notice().orElse(null))
            .build();
    }

    static InlineKeyboardMarkup toMarkup(InlineKeyboard keyboard) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        for (List<InlineButton> row : keyboard.rows()) {
            InlineKeyboardRow telegramRow = new InlineKeyboardRow();
            for (InlineButton button : row) {
                telegramRow.add(InlineKeyboardButton.builder()
                    .text(button.text())
                    .callbackData(button.callbackData())
                    .build());
            }
            rows.add(telegramRow);
        }
        return new InlineKeyboardMarkup(rows);
    }
}
