package ru.freedomminecraft.bot.reply;

import java.util.Objects;
import java.util.Optional;

public record Reply(String text, Optional<InlineKeyboard> keyboard) {
    public Reply {
        Objects.requireNonNull(text, "Не задан текст ответа");
        Objects.requireNonNull(keyboard, "Клавиатура не задана: если её нет, нужен Optional.empty()");

        if (text.isBlank()) {
            throw new IllegalArgumentException("Текст ответа пустой - Telegram не отправит");
        }
    }

    /** Только с текстом */
    public static Reply of(String text) {
        return new Reply(text, Optional.empty());
    }

    /** С текстом и клавиатурой */
    public static Reply withKeyboard(String text, InlineKeyboard keyboard) {
        return new Reply(text, Optional.of(keyboard));
    }
}
