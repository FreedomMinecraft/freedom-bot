package ru.freedomminecraft.bot.callback;

import java.util.Objects;
import java.util.Optional;

import ru.freedomminecraft.bot.reply.Reply;

/**
 * Итог нажатия кнопки.
 *
 * @param notice короткое уведомление вверху чата без разметки
 * @param update новое содержимое сообщения с кнопкой
 * */

public record CallbackResult(Optional<String> notice, Optional<Reply> update) {
    public static final int MAX_NOTICE_LENGTH = 200;

    public CallbackResult {
        Objects.requireNonNull(notice, "Уведомление не задано: если его нет, нужен Optional.empty()");
        Objects.requireNonNull(update, "Новое содержимое не задано: если его нет, нужен Optional.empty()");
        if (notice.isPresent()) {
            String text = notice.get();
            if (text.isBlank() || text.length() > MAX_NOTICE_LENGTH) {
                throw new IllegalArgumentException("Уведомление должно занимать 1-" + MAX_NOTICE_LENGTH + " символов, а занимает " + text.length());
            }
        }
    }

    public static CallbackResult silent() {
        return new CallbackResult(Optional.empty(), Optional.empty());
    }

    public static CallbackResult notice(String text) {
        return new CallbackResult(Optional.of(text), Optional.empty());
    }

    public static CallbackResult update(Reply reply) {
        return new CallbackResult(Optional.empty(), Optional.of(reply));
    }
}
