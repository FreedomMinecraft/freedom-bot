package ru.freedomminecraft.bot.command;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.freedomminecraft.bot.serverinfo.Link;
import ru.freedomminecraft.bot.serverinfo.StaffGroup;
import ru.freedomminecraft.bot.serverinfo.TeamMember;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StaffCommandTest {
    private final StaffCommand admins = new StaffCommand(
        "admins", "Админ-состав сервера", "Админ-состав FREEDOM:",
        List.of(new StaffGroup("Администратор", List.of(
                new TeamMember("pacan1337", List.of()))),
            new StaffGroup("Модераторы", List.of(
                new TeamMember("pacetko228", List.of()),
                new TeamMember("batryxa67", List.of())))));

    @Test
    void nameComesFromConstructor(){
        assertEquals("admins", admins.name());
    }

    @Test
    void showsGroupTitles(){
        String answer = admins.execute("");

        assertTrue(answer.contains("Администратор:"));
        assertTrue(answer.contains("Модераторы:"));
    }

    @Test
    void listsEveryMember(){
        String answer = admins.execute("");

        assertTrue(answer.contains("pacan1337"));
        assertTrue(answer.contains("pacetko228"));
        assertTrue(answer.contains("batryxa67"));
    }

    @Test
    void membersGoUnderTheirGroup(){
        String answer = admins.execute("");

        assertTrue(answer.indexOf("Модераторы:") < answer.indexOf("pacetko228"));
        assertTrue(answer.indexOf("pacan1337") < answer.indexOf("Модераторы:"));
    }

    @Test
    void showMemberLinks(){
        StaffCommand media = new StaffCommand(
            "media", "Медиа-состав сервера", "Медиа-состав FREEDOM:",
            List.of(new StaffGroup("Медиа", List.of(
                new TeamMember("apollodance", List.of(new Link("Twitch", "https://www.twitch.tv/test")))))));

        String answer = media.execute("");

        assertTrue(answer.contains("Twitch: https://www.twitch.tv/test"));
    }

    @Test
    void escapesGroupAndMemberData(){
        StaffCommand staff = new StaffCommand(
            "admins", "Админ-состав сервера", "Админ-состав FREEDOM:",
            List.of(new StaffGroup("Модераторы & хелперы", List.of(
                new TeamMember("<Ник>", List.of())))));

        String answer = staff.execute("");

        assertTrue(answer.contains("Модераторы &amp; хелперы:"));
        assertTrue(answer.contains("&lt;Ник&gt;"));
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
