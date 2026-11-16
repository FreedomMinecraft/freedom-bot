package ru.freedomminecraft.bot.callback;

import org.junit.jupiter.api.Test;

import ru.freedomminecraft.bot.reply.Reply;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CallbackResultTest {

    @Test
    void silentHasNothing() {
        CallbackResult result = CallbackResult.silent();

        assertTrue(result.notice().isEmpty());
        assertTrue(result.update().isEmpty());
    }

    @Test
    void noticeKeepsText() {
        CallbackResult result = CallbackResult.notice("Готово");

        assertEquals("Готово", result.notice().orElseThrow());
        assertTrue(result.update().isEmpty());
    }

    @Test
    void updateKeepsReply() {
        CallbackResult result = CallbackResult.update(Reply.of("новый текст"));

        assertEquals(Reply.of("новый текст"), result.update().orElseThrow());
        assertTrue(result.notice().isEmpty());
    }

    @Test
    void rejectsBlankNotice() {
        assertThrows(IllegalArgumentException.class, () -> CallbackResult.notice("  "));
    }

    @Test
    void rejectsTooLongNotice() {
        String tooLong = "а".repeat(CallbackResult.MAX_NOTICE_LENGTH + 1);

        assertThrows(IllegalArgumentException.class, () -> CallbackResult.notice(tooLong));
    }
}
