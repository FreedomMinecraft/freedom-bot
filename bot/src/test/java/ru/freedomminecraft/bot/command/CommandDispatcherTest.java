package ru.freedomminecraft.bot.command;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.freedomminecraft.bot.command.basic.PingCommand;

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
        public String execute(CommandContext context) {
            return context.argument();
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
        public String execute(CommandContext context){
            throw new IllegalStateException("команда сломалась");
        }
    }

    private static class WhoAmICommand implements Command {
        @Override
        public String name() {
            return "whoami";
        }

        @Override
        public String description() {
            return "Показывает, из какого чата и от кого пришла команда";
        }

        @Override
        public String execute(CommandContext context) {
            return context.chatId() + " " + context.userId();
        }
    }

    @BeforeEach
    void setUp() {
        CommandRegistry registry = new CommandRegistry();
        registry.register(new PingCommand());
        registry.register(new EchoCommand());
        registry.register(new BrokenCommand());
        registry.register(new WhoAmICommand());
        dispatcher = new CommandDispatcher(new CommandParser(), registry);
    }

    @Test
    void executesKnownCommand(){
        assertEquals(Optional.of("pong"), dispatcher.dispatch(TestContext.CHAT_ID, TestContext.USER_ID, "/ping"));
    }

    @Test
    void passedArgumentToCommand() {
        assertEquals(Optional.of("всем привет"), dispatcher.dispatch(TestContext.CHAT_ID, TestContext.USER_ID, "/echo всем привет"));
    }

    @Test
    void passesChatAndUserToCommand() {
        assertEquals(Optional.of("42 7"), dispatcher.dispatch(42, 7, "/whoami"));
    }

    @Test
    void repliesToUnknownCommand() {
        String answer = dispatcher.dispatch(TestContext.CHAT_ID, TestContext.USER_ID, "/ogogo").orElseThrow();

        assertTrue(answer.contains("Не знаю команду /ogogo"));
        assertTrue(answer.contains("/help"));
    }

    @Test
    void escapesUnknownCommandName() {
        String answer = dispatcher.dispatch(TestContext.CHAT_ID, TestContext.USER_ID, "/<b>").orElseThrow();

        assertTrue(answer.contains("Не знаю команду /&lt;b&gt;"));
    }

    @Test
    void ignoresPlainText() {
        assertTrue(dispatcher.dispatch(TestContext.CHAT_ID, TestContext.USER_ID, "привет").isEmpty());
    }

    @Test
    void brokenCommandDoesNotThrow(){
        assertDoesNotThrow(() -> dispatcher.dispatch(TestContext.CHAT_ID, TestContext.USER_ID, "/broken"));
    }

    @Test
    void repliesWithErrorWhenCommandFails() {
        String answer = dispatcher.dispatch(TestContext.CHAT_ID, TestContext.USER_ID, "/broken").orElseThrow();

        assertTrue(answer.contains("Не получилось выполнить /broken"));
    }

    @Test
    void worksAfterBrokenCommand(){
        dispatcher.dispatch(TestContext.CHAT_ID, TestContext.USER_ID, "/broken");
        assertEquals(Optional.of("pong"), dispatcher.dispatch(TestContext.CHAT_ID, TestContext.USER_ID, "/ping"));
    }
}
