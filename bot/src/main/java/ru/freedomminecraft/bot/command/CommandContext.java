package ru.freedomminecraft.bot.command;

/**
 *
 * @param chatId чат, куда пойдет ответ
 *
 * @param userId Telegram id пользователя, написавшего команду
 *
 * @param argument текст после имени команды.
 *
 */

public record CommandContext(long chatId, long userId, String argument) {}
