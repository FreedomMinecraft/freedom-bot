package ru.freedomminecraft.bot.command;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandRegistryTest {

    private final CommandRegistry registry = new CommandRegistry();

    @Test
    void findsRegisteredCommandByName() {
        PingCommand ping = new PingCommand();
        registry.register(ping);

        Optional<Command> found = registry.find("ping");

        assertTrue(found.isPresent());
        assertSame(ping, found.get());
    }

    @Test
    void returnsEmptyForUnknownCommand() {
        assertTrue(registry.find("asdfsdfg").isEmpty());
    }

    @Test
    void rejectDuplicateName() {
        registry.register(new PingCommand());

        assertThrows(IllegalArgumentException.class, () -> registry.register(new PingCommand()));
    }

    @Test
    void allReturnsRegisterCommands() {
        registry.register(new PingCommand());

        List<Command> all = registry.all();

        assertEquals(1, all.size());
        assertEquals("ping", all.get(0).name());
    }
}
