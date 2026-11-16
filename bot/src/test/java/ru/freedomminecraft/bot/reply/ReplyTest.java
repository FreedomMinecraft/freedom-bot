package ru.freedomminecraft.bot.reply;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReplyTest {

    @Test
    void textReplyHasNoKeyboard() {
        Reply reply = Reply.of("pong");

        assertEquals("pong", reply.text());
        assertTrue(reply.keyboard().isEmpty());
    }

    @Test
    void replyWithKeyboardKeepsIt() {
        InlineKeyboard keyboard = new InlineKeyboard(List.of(List.of(new InlineButton("Да", "yes"))));

        Reply reply = Reply.withKeyboard("Подписаться?", keyboard);

        assertEquals("Подписаться?", reply.text());
        assertEquals(keyboard, reply.keyboard().orElseThrow());
    }

    @Test
    void rejectsBlankText() {
        assertThrows(IllegalArgumentException.class, () -> Reply.of("   "));
    }

    @Test
    void rejectsMissingText() {
        assertThrows(NullPointerException.class, () -> Reply.of(null));
    }

    @Test
    void sameTextMeansEqualReplies() {
        assertEquals(Reply.of("pong"), Reply.of("pong"));
    }
}
