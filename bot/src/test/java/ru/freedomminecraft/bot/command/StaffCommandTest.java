package ru.freedomminecraft.bot.command;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.freedomminecraft.bot.serverinfo.Link;
import ru.freedomminecraft.bot.serverinfo.TeamMember;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StaffCommandTest {
    private final StaffCommand admins = new StaffCommand(
        "admins", "Админ-состав сервера", "Админ-состав FREEDOM:",
        List.of(new TeamMember("pacan1337", "Владелец", List.of()),
                new TeamMember("pacetko228", "Модератор", List.of())));

    @Test
    void nameComesFromConstructor(){
        assertEquals("admins", admins.name());
    }

    @Test
    void listsEveryMember(){
        String answer = admins.execute("");

        assertTrue(answer.contains("pacan1337 — Владелец"));
        assertTrue(answer.contains("pacetko228 — Модератор"));
    }

    @Test
    void showMemberLinks(){
        StaffCommand media = new StaffCommand(
            "media", "Медиа-состав сервера", "Медиа-состав FREEDOM:",
            List.of(new TeamMember("Streamer", "Медиа",
                List.of(new Link("Twitch", "https://www.twitch.tv/test")))));

        String answer = media.execute("");

        assertTrue(answer.contains("Twitch: https://www.twitch.tv/test"));
    }

    @Test
    void emptyListSaysSo(){
        StaffCommand empty = new StaffCommand("admins", "Админ-состав сервера", "Админ-состав FREEDOM:", List.of());

        assertTrue(empty.execute("").contains("пуст"));
    }

    @Test
    void appearsInHelpAutomatically(){
        CommandRegistry registry = new CommandRegistry();
        registry.register(new HelpCommand(registry));
        registry.register(admins);

        String helpAnswer = registry.find("help").get().execute("");

        assertTrue(helpAnswer.contains("/admins - Админ-состав сервера"));
    }
}
