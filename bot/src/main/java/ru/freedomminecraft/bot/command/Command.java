package ru.freedomminecraft.bot.command;

public interface Command {
    String name();
    /**Обычный текст, без разметки*/
    String description();
    /**Ответ бота в разметке HTML
     * Данные и ввод через Html.escape()
     */
    String execute(CommandContext context);

    default CommandGroup group() {
        return CommandGroup.OTHER;
    }
}
