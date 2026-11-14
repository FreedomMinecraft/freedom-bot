package ru.freedomminecraft.bot.serverinfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileServerInfoSourceTest {
    private static final String VALID_JSON = """
            {
                "admins": [ { "title": "Администратор", "members": [ { "nickname": "FileAdmin" } ] } ],
                "eventors": [],
                "media": [],
                "chats": [ { "name": "Чат", "description": "Тестовый", "url": "https://t.me/test" } ],
                "support": []
            }
            """;
    @TempDir
    Path folder;
    @Test
    void loadsDataFromFile() throws IOException {
        Path file = folder.resolve("server-info.json");
        Files.writeString(file, VALID_JSON);

        ServerInfo info = new FileServerInfoSource(file).load();

        assertEquals("FileAdmin", info.admins().getFirst().members().getFirst().nickname());
        assertEquals("Чат", info.chats().getFirst().name());
    }

    @Test
    void missingFileThrows() {
        Path file = folder.resolve("no-such-file.json");

        IllegalStateException e = assertThrows(IllegalStateException.class,
            () -> new FileServerInfoSource(file).load());
        assertTrue(e.getMessage().startsWith("Не найден файл"));
    }

    @Test
    void brokenJsonThrows() throws IOException {
        Path file = folder.resolve("server-info.json");
        Files.writeString(file, "{ \"admins\": [ ");

        assertThrows(IllegalStateException.class, () -> new FileServerInfoSource(file).load());
    }

    @Test
    void toStringShowsPath() {
        Path file = folder.resolve("server-info.json");

        assertTrue(new FileServerInfoSource(file).toString().contains("server-info.json"));
    }
}
