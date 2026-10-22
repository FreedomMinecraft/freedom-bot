package ru.freedomminecraft.bot.serverinfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class JsonServerInfoSourceTest {

    private static ServerInfo load(String resource) {
        return new JsonServerInfoSource(resource).load();
    }

    @Test
    void loadsAllSections() {
        ServerInfo info = load("/server-info-test.json");

        assertEquals(2, info.admins().size());
        assertEquals("TestAdmin", info.admins().get(0).nickname());
        assertEquals("Администратор", info.admins().get(0).position());
        assertEquals(1, info.eventors().size());
        assertEquals(1, info.media().size());
        assertEquals("Тестовый чат", info.chats().get(0).name());
        assertEquals("https://t.me/test_bot", info.support().get(0).url());
    }

    @Test
    void readsMediaLinks() {
        TeamMember streamer = load("/server-info-test.json").media().get(0);

        assertEquals("Twitch", streamer.links().get(0).title());
        assertEquals("https://www.twitch.tv/test", streamer.links().get(0).url());
    }

    @Test
    void memberWithoutLinksGetsEmptyList() {
        TeamMember admin = load("/server-info-test.json").admins().get(0);

        assertTrue(admin.links().isEmpty());
    }

    @Test
    void missingFileThrows() {
        assertThrows(IllegalStateException.class, () -> load("/no-such-file.json"));
    }

    @Test
    void fileWithoutSectionThrows() {
        assertThrows(IllegalStateException.class, () -> load("/server-info-without-chats.json"));
    }
}
