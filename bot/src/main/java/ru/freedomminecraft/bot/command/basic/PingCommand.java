package ru.freedomminecraft.bot.command.basic;

import ru.freedomminecraft.bot.command.Command;
import ru.freedomminecraft.bot.command.CommandContext;
import ru.freedomminecraft.bot.reply.Reply;

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
    public Reply execute(CommandContext context) {
        return Reply.of("pong");
    }
}
