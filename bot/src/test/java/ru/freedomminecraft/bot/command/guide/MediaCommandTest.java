package ru.freedomminecraft.bot.command.guide;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.freedomminecraft.bot.command.CommandRegistry;
import ru.freedomminecraft.bot.command.basic.HelpCommand;
import ru.freedomminecraft.bot.serverinfo.StaffGroup;
import ru.freedomminecraft.bot.serverinfo.TeamMember;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MediaCommandTest {
    private final MediaCommand media = new MediaCommand(List.of(
        new StaffGroup("Тестовая группа", List.of(new TeamMember("TestMember", List.of())))));

    @Test
    void nameIsMedia(){
        assertEquals("media", media.name());
    }

    @Test
    void startsWithTitle(){
        String answer = media.execute("");

        assertTrue(answer.indexOf("МЕДИА FREEDOM") < answer.indexOf("TestMember"));
    }

    @Test
    void appearsInHelpAutomatically(){
        CommandRegistry registry = new CommandRegistry();
        registry.register(new HelpCommand(registry));
        registry.register(media);

        String helpAnswer = registry.find("help").get().execute("");

        assertTrue(helpAnswer.contains("/media — Стримеры и блогеры сервера"));
    }
}
