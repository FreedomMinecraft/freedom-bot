package ru.freedomminecraft.bot.command;

import org.junit.jupiter.api.Test;

import javax.print.attribute.HashPrintServiceAttributeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PingCommandTest {

    private final PingCommand ping = new PingCommand();

    @Test
    void nameIsPing(){
        assertEquals("ping", ping.name());
    }

    @Test
    void answersPong(){
        assertEquals("pong", ping.execute("123"));
    }

    @Test
    void ignoresArgument() {
        assertEquals("pong", ping.execute("123"));
    }
}
