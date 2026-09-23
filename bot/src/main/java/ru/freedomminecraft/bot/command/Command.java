package ru.freedomminecraft.bot.command;

public interface Command {
    String name();
    String description();

    String execute(String argument);
}
