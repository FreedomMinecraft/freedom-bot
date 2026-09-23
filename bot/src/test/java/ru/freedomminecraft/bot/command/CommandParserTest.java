package ru.freedomminecraft.bot.command;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandParserTest {
    private final CommandParser parser = new CommandParser();

    @Test
    void parsesCommandWithoutArgument() {
        assertEquals(Optional.of(new ParsedCommand("ping", "")), parser.parse("/ping"));
    }

    @Test
    void parsesCommandWithArgument() {
        assertEquals(Optional.of(new ParsedCommand("help", "about")), parser.parse("/help about"));
    }

    @Test
    void keepsWholeRestAsArgument() {
        assertEquals(Optional.of(new ParsedCommand("clan", "ERRIS Clan")), parser.parse("/clan ERRIS Clan"));
    }

    @Test
    void ignoresExtraSpaces() {
        assertEquals(Optional.of(new ParsedCommand("help", "about")), parser.parse("   /help   about   "));
    }

    @Test
    void removesBotMention() {
        assertEquals(Optional.of(new ParsedCommand("help", "about")), parser.parse("/help@FreedomBot about"));
    }

    @Test
    void ignoresCase() {
        assertEquals(Optional.of(new ParsedCommand("ping", "")), parser.parse("/PING"));
    }

    @Test
    void plainTextIsNotCommand() {
        assertTrue(parser.parse("привет").isEmpty());
    }

    @Test
    void slashTextIsNotCommand() {
        assertTrue(parser.parse("/").isEmpty());
    }

    @Test
    void nullIsNotCommand() {
        assertTrue(parser.parse(null).isEmpty());
    }
}
