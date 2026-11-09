package ru.freedomminecraft.bot.command.guide;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.freedomminecraft.bot.command.CommandRegistry;
import ru.freedomminecraft.bot.command.basic.HelpCommand;
import ru.freedomminecraft.bot.serverinfo.StaffGroup;
import ru.freedomminecraft.bot.serverinfo.TeamMember;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventorsCommandTest {
    private final EventorsCommand eventors = new EventorsCommand(List.of(
        new StaffGroup("Тестовая группа", List.of(new TeamMember("TestMember", List.of())))));

    @Test
    void nameIsEventors(){
        assertEquals("eventors", eventors.name());
    }

    @Test
    void startsWithTitle(){
        String answer = eventors.execute("");

        assertTrue(answer.indexOf("ИВЕНТОРЫ FREEDOM") < answer.indexOf("TestMember"));
    }

    @Test
    void appearsInHelpAutomatically(){
        CommandRegistry registry = new CommandRegistry();
        registry.register(new HelpCommand(registry));
        registry.register(eventors);

        String helpAnswer = registry.find("help").get().execute("");

        assertTrue(helpAnswer.contains("/eventors — Ивенторы сервера"));
    }
}
