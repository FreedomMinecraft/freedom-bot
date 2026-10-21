package ru.freedomminecraft.bot.command;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandDispatcher {
    private static final Logger log = LoggerFactory.getLogger(CommandDispatcher.class);

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
            log.info("Неизвестная команда: /{}", parsedCommand.name());
            return Optional.of("Неизвестная команда /" + parsedCommand.name() + ". Список команд: /help");
        }

        log.info("Команда: /{}", parsedCommand.name());
        try {
            return Optional.of(command.get().execute(parsedCommand.argument()));
        } catch (RuntimeException e){
            log.error("Ошибка при выполнении команды /{}", parsedCommand.name(), e);
            return Optional.of("Ошибка при выполнении команды /" + parsedCommand.name() + "Попробуйте позже");
        }
    }
}
