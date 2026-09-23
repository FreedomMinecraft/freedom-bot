package ru.freedomminecraft.bot.command;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandDispatcherTest {
    private CommandDispatcher dispatcher;

    private static class EchoCommand implements Command {
        @Override
        public String name() {
            return "echo";
        }

        @Override
        public String description() {
            return "Повторяет аргумент";
        }

        @Override
        public String execute(String argument) {
            return argument;
        }
    }

    @BeforeEach
    void setUp() {
        CommandRegistry registry = new CommandRegistry();
        registry.register(new PingCommand());
        registry.register(new EchoCommand());
        dispatcher = new CommandDispatcher(new CommandParser(), registry);
    }

    @Test
    void executesKnownCommand(){
        assertEquals(Optional.of("pong"), dispatcher.dispatch("/ping"));
    }

    @Test
    void passedArgumentToCommand() {
        assertEquals(Optional.of("всем привет"), dispatcher.dispatch("/echo всем привет"));
    }

    @Test
    void repliesToUnknownCommand() {
        assertEquals(Optional.of("Неизвестная команда /ogogo. Список команд: /help"), dispatcher.dispatch("/ogogo"));
    }

    @Test
    void ignoresPlainText() {
        assertTrue(dispatcher.dispatch("привет").isEmpty());
    }
}
