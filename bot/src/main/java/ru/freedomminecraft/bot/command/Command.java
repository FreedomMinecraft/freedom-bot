package ru.freedomminecraft.bot.command;

import ru.freedomminecraft.bot.reply.Reply;

public interface Command {
    String name();
    /**Обычный текст, без разметки*/
    String description();
    /**Ответ бота в разметке HTML
     * Данные и ввод через Html.escape()
     */
    Reply execute(CommandContext context);

    default CommandGroup group() {
        return CommandGroup.OTHER;
    }
}
