package ru.freedomminecraft.bot.reply;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Inline-кнопка
 *
 * @param text         надпись на кнопке
 * @param callbackData что бот получит при нажатии
 * */

public record InlineButton(String text, String callbackData) {
    public static final int MAX_CALLBACK_BYTES = 64;

    public InlineButton {
        Objects.requireNonNull(text, "Не задана надпись на кнопке");
        Objects.requireNonNull(callbackData, "Не заданы данные кнопки");
        if (text.isBlank()) {
            throw new IllegalArgumentException("Надпись на кнопке пустая");
        }
        int bytes = callbackData.getBytes(StandardCharsets.UTF_8).length;
        if (bytes == 0 || bytes > MAX_CALLBACK_BYTES) {
            throw new IllegalArgumentException("Данные кнопки должны занимать 1–" + MAX_CALLBACK_BYTES
                + " байт, а занимают " + bytes + ": " + callbackData);
        }
    }
}
