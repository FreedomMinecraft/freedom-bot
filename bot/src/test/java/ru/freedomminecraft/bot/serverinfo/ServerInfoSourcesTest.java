package ru.freedomminecraft.bot.serverinfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Map;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class ServerInfoSourcesTest {

    @Test
    void withoutVariableUsesResource() {
        ServerInfoSource source = ServerInfoSources.fromEnvironment(Map.of());

        assertInstanceOf(JsonServerInfoSource.class, source);
    }

    @Test
    void blankVariableUsesResource() {
        ServerInfoSource source = ServerInfoSources.fromEnvironment(Map.of("SERVER_INFO_FILE", "   "));

        assertInstanceOf(JsonServerInfoSource.class, source);
    }

    @Test
    void variableSetUsesFile() {
        ServerInfoSource source = ServerInfoSources.fromEnvironment(Map.of("SERVER_INFO_FILE", "/app/data/server-info.json"));

        assertInstanceOf(FileServerInfoSource.class, source);
        assertEquals("файл " + Path.of("/app/data/server-info.json"), source.toString());
    }
}
