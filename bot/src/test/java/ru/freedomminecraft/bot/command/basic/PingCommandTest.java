package ru.freedomminecraft.bot.command.basic;

import org.junit.jupiter.api.Test;
import ru.freedomminecraft.bot.command.TestContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PingCommandTest {

    private final PingCommand ping = new PingCommand();

    @Test
    void nameIsPing(){
        assertEquals("ping", ping.name());
    }

    @Test
    void answersPong(){
        assertEquals("pong", ping.execute(TestContext.of("")).text());
    }

    @Test
    void ignoresArgument() {
        assertEquals("pong", ping.execute(TestContext.of("123")).text());
    }
}
