package ru.freedomminecraft.bot.reply;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InlineButtonTest {

    @Test
    void acceptsNormalButton() {
        assertDoesNotThrow(() -> new InlineButton("Ивенты", "sub:EVENT"));
    }

    @Test
    void rejectsBlankText() {
        assertThrows(IllegalArgumentException.class, () -> new InlineButton("  ", "sub:EVENT"));
    }

    @Test
    void rejectsEmptyCallbackData() {
        assertThrows(IllegalArgumentException.class, () -> new InlineButton("Ивенты", ""));
    }

    @Test
    void acceptsExactly64Bytes() {
        assertDoesNotThrow(() -> new InlineButton("Кнопка", "a".repeat(64)));
    }

    @Test
    void rejectsMoreThan64Bytes() {
        assertThrows(IllegalArgumentException.class, () -> new InlineButton("Кнопка", "a".repeat(65)));
    }

    @Test
    void countsBytesNotCharacters() {
        assertThrows(IllegalArgumentException.class, () -> new InlineButton("Кнопка", "я".repeat(40)));
    }
}
