package ru.freedomminecraft.bot.telegram;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

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
}
