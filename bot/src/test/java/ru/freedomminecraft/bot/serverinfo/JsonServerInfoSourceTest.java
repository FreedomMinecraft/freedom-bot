package ru.freedomminecraft.bot.serverinfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class JsonServerInfoSourceTest {

    private static ServerInfo load(String resource) {
        return new JsonServerInfoSource(resource).load();
    }

    @Test
    void loadsAllSections() {
        ServerInfo info = load("/server-info-test.json");

        assertEquals(2, info.admins().size());
        assertEquals("Администратор", info.admins().getFirst().title());
        assertEquals("TestAdmin", info.admins().getFirst().members().getFirst().nickname());
        assertEquals(1, info.eventors().size());
        assertEquals(1, info.media().size());
        assertEquals("Тестовый чат", info.chats().getFirst().name());
        assertEquals("https://t.me/test_bot", info.support().getFirst().url());
    }

    @Test
    void keepsGroupsAndMembersInFileOrder() {
        StaffGroup moderators = load("/server-info-test.json").admins().get(1);

        assertEquals("Модераторы", moderators.title());
        assertEquals("TestModer", moderators.members().get(0).nickname());
        assertEquals("TestModer2", moderators.members().get(1).nickname());
    }

    @Test
    void readsMediaLinks() {
        TeamMember streamer = load("/server-info-test.json").media().getFirst().members().getFirst();

        assertEquals("Twitch", streamer.links().getFirst().title());
        assertEquals("https://www.twitch.tv/test", streamer.links().getFirst().url());
    }

    @Test
    void memberWithoutLinksGetsEmptyList() {
        TeamMember admin = load("/server-info-test.json").admins().getFirst().members().getFirst();

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

    @Test
    void groupWithoutMembersThrows() {
        assertThrows(IllegalStateException.class, () -> load("/server-info-group-without-members.json"));
    }

    @Test
    void realDataFileIsValid() {
        ServerInfo info = load(JsonServerInfoSource.DEFAULT_RESOURCE);

        assertFalse(info.admins().isEmpty());
        assertFalse(info.chats().isEmpty());
    }
}
