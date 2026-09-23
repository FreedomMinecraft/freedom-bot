package ru.freedomminecraft.bot.command;

import java.util.Optional;

public class CommandDispatcher {
    private final CommandParser parser;
    private final CommandRegistry registry;

    public CommandDispatcher (CommandParser parser, CommandRegistry registry) {
        this.parser = parser;
        this.registry = registry;
    }

    public Optional<String> dispatch(String text) {
        Optional<ParsedCommand> parsed = parser.parse(text);
        if (parsed.isEmpty()) {
            return Optional.empty();
        }

        ParsedCommand parsedCommand = parsed.get();
        Optional<Command> command = registry.find(parsedCommand.name());
        if (command.isEmpty()) {
            return Optional.of("Неизвестная команда /" + parsedCommand.name() + ". Список команд: /help");
        }

        return Optional.of(command.get().execute(parsedCommand.argument()));
    }
}
