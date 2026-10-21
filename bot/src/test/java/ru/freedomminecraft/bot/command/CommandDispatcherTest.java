package ru.freedomminecraft.bot.command;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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

    private static class BrokenCommand implements Command {
        @Override
        public String name(){
            return "broken";
        }

        @Override
        public String description(){
            return "всегда падает с ошибкой";
        }

        @Override
        public String execute(String argument){
            throw new IllegalStateException("команда сломалась");
        }
    }

    @BeforeEach
    void setUp() {
        CommandRegistry registry = new CommandRegistry();
        registry.register(new PingCommand());
        registry.register(new EchoCommand());
        registry.register(new BrokenCommand());
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

    @Test
    void brokenCommandDoesNotThrow(){
        assertDoesNotThrow(() -> dispatcher.dispatch("/broken"));
    }

    @Test
    void repliesWithErrorWhenCommandFails() {
        assertEquals(
            Optional.of("ошибка при выполнении команды /broken. попробуйте позже."),
            dispatcher.dispatch("/broken"));
    }

    @Test
    void worksAfterBrokenCommand(){
        dispatcher.dispatch("/broken");
        assertEquals(Optional.of("pong"), dispatcher.dispatch("/ping"));
    }
}
