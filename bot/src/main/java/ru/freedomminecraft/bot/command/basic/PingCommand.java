package ru.freedomminecraft.bot.command.basic;

import ru.freedomminecraft.bot.command.Command;
import ru.freedomminecraft.bot.command.CommandContext;

public class PingCommand implements Command {
    @Override
    public String name() {
        return "ping";
    }

    @Override
    public String description() {
        return "Проверить, что бот на связи";
    }

    @Override
    public String execute(CommandContext context) {
        return "pong";
    }
}
