package ru.freedomminecraft.bot.command.guide;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.freedomminecraft.bot.command.CommandRegistry;
import ru.freedomminecraft.bot.command.TestContext;
import ru.freedomminecraft.bot.command.basic.HelpCommand;
import ru.freedomminecraft.bot.format.Html;
import ru.freedomminecraft.bot.serverinfo.Link;
import ru.freedomminecraft.bot.serverinfo.StaffGroup;
import ru.freedomminecraft.bot.serverinfo.TeamMember;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminsCommandTest {
    private final List<StaffGroup> groups = List.of(
        new StaffGroup("Администратор", List.of(new TeamMember("pacan1337", List.of()))));

    private final AdminsCommand admins = new AdminsCommand(groups,
        List.of(new Link("Бот поддержки", "https://t.me/test_bot")));

    @Test
    void nameIsAdmins(){
        assertEquals("admins", admins.name());
    }

    @Test
    void showsAdmins(){
        assertTrue(admins.execute(TestContext.of("")).text().contains("pacan1337"));
    }

    @Test
    void endsWithSupportLinks(){
        String answer = admins.execute(TestContext.of("")).text();

        assertTrue(answer.contains("Нужна помощь?"));
        assertTrue(answer.endsWith(Html.link("Бот поддержки", "https://t.me/test_bot")));
    }

    @Test
    void noSupportNoFooter(){
        AdminsCommand withoutSupport = new AdminsCommand(groups, List.of());

        assertFalse(withoutSupport.execute(TestContext.of("")).text().contains("Нужна помощь?"));
    }

    @Test
    void appearsInHelpAutomatically(){
        CommandRegistry registry = new CommandRegistry();
        registry.register(new HelpCommand(registry));
        registry.register(admins);

        String helpAnswer = registry.find("help").get().execute(TestContext.of("")).text();

        assertTrue(helpAnswer.contains("/admins — Администрация и поддержка"));
    }
}
