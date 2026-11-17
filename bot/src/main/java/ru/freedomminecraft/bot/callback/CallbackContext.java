package ru.freedomminecraft.bot.callback;

/**
 * Нажатие inline-кнопки.
 *
 * @param chatId    чат, где висит сообщение с кнопкой
 * @param userId    Телеграм id пользователя, нажавшего кнопку
 * @param messageId сообщение с кнопкой
 * @param payload   данные кнопки после метки обработчика
 * */

public record CallbackContext(long chatId, long userId, int messageId, String payload) {}
