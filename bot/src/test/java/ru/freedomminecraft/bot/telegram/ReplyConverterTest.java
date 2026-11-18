package ru.freedomminecraft.bot.telegram;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import ru.freedomminecraft.bot.callback.CallbackResult;
import ru.freedomminecraft.bot.reply.InlineButton;
import ru.freedomminecraft.bot.reply.InlineKeyboard;
import ru.freedomminecraft.bot.reply.Reply;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReplyConverterTest {

    @Test
    void copiesChatAndText() {
        SendMessage message = ReplyConverter.toSendMessage(42, Reply.of("<b>pong</b>"));

        assertEquals("42", message.getChatId());
        assertEquals("<b>pong</b>", message.getText());
    }

    @Test
    void sendsHtmlWithoutLinkPreviews() {
        SendMessage message = ReplyConverter.toSendMessage(42, Reply.of("pong"));

        assertEquals(ParseMode.HTML, message.getParseMode());
        assertTrue(message.getLinkPreviewOptions().getIsDisabled());
    }

    @Test
    void textReplyHasNoKeyboard() {
        SendMessage message = ReplyConverter.toSendMessage(42, Reply.of("pong"));

        assertNull(message.getReplyMarkup());
    }

    @Test
    void keyboardKeepsRowsButtonsAndData() {
        InlineKeyboard keyboard = new InlineKeyboard(List.of(
            List.of(new InlineButton("Да", "yes"), new InlineButton("Нет", "no")),
            List.of(new InlineButton("Отмена", "cancel"))));

        SendMessage message = ReplyConverter.toSendMessage(42, Reply.withKeyboard("Подписаться?", keyboard));

        InlineKeyboardMarkup markup = assertInstanceOf(InlineKeyboardMarkup.class, message.getReplyMarkup());
        List<InlineKeyboardRow> rows = markup.getKeyboard();
        assertEquals(2, rows.size());
        assertEquals(2, rows.getFirst().size());
        assertEquals("Да", rows.getFirst().getFirst().getText());
        assertEquals("yes", rows.get(0).get(0).getCallbackData());
        assertEquals("no", rows.get(0).get(1).getCallbackData());
        assertEquals("Отмена", rows.get(1).getFirst().getText());
        assertEquals("cancel", rows.get(1).getFirst().getCallbackData());
    }

    @Test
    void editKeepsChatMessageTextAndKeyboard() {
        InlineKeyboard keyboard = new InlineKeyboard(List.of(List.of(new InlineButton("Да", "yes"))));

        EditMessageText edit = ReplyConverter.toEditMessage(42,7,Reply.withKeyboard("<b>Подписки</b>", keyboard));

        assertEquals("42", edit.getChatId());
        assertEquals(7, edit.getMessageId());
        assertEquals("<b>Подписки</b>", edit.getText());
        assertEquals(ParseMode.HTML, edit.getParseMode());
        assertEquals("yes", edit.getReplyMarkup().getKeyboard().getFirst().getFirst().getCallbackData());
    }

    @Test
    void editWithoutKeyboardRemovesButtons() {
        EditMessageText edit = ReplyConverter.toEditMessage(42,7,Reply.of("Готово"));

        assertNull(edit.getReplyMarkup());
    }

    @Test
    void silentAnswerHasNoText() {
        AnswerCallbackQuery answer = ReplyConverter.toAnswer("query-1", CallbackResult.silent());

        assertEquals("query-1", answer.getCallbackQueryId());
        assertNull(answer.getText());
    }

    @Test
    void answerShowsNotice() {
        AnswerCallbackQuery answer = ReplyConverter.toAnswer("query-1", CallbackResult.notice("Готово"));

        assertEquals("Готово", answer.getText());
    }
}
