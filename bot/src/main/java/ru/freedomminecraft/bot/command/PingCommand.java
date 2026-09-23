package ru.freedomminecraft.bot.command;

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
    public String execute(String argument) {
        return "pong";
    }
}
