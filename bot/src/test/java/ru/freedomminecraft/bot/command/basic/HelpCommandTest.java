package ru.freedomminecraft.bot.command.basic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.freedomminecraft.bot.command.CommandGroup;
import ru.freedomminecraft.bot.command.CommandRegistry;
import ru.freedomminecraft.bot.command.TestContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HelpCommandTest {
    private CommandRegistry registry;
    private HelpCommand help;

    @BeforeEach
    void setUp(){
        registry = new CommandRegistry();
        help = new HelpCommand(registry);
        registry.register(help);
    }

    @Test
    void nameIsHelp(){
        assertEquals("help", help.name());
    }

    @Test
    void newCommandAppearsInHelpAutomatically(){
        registry.register(new PingCommand());

        String answer = help.execute(TestContext.of(""));

        assertTrue(answer.contains("/ping"));
        assertTrue(answer.contains("Проверить, что бот на связи"));
    }

    @Test
    void nullArgumentWorksLikeEmpty(){
        registry.register(new PingCommand());

        assertEquals(help.execute(TestContext.of("")), help.execute(TestContext.of(null)));
    }

    @Test
    void showsHelpForOneCommand(){
        registry.register(new PingCommand());

        String answer = help.execute(TestContext.of("ping"));

        assertEquals("/ping — Проверить, что бот на связи", answer);
    }

    @Test
    void ignoresSpacesAroundCommandName(){
        registry.register(new PingCommand());

        assertEquals(help.execute(TestContext.of("ping")), help.execute(TestContext.of(" ping ")));
    }

    @Test
    void reportsUnknownCommand(){
        String answer = help.execute(TestContext.of("fly"));

        assertTrue(answer.contains("Нет команды «fly»"));
    }

    @Test
    void acceptsCommandNameWithSlash() {
        registry.register(new PingCommand());

        assertEquals(help.execute(TestContext.of("ping")), help.execute(TestContext.of("/ping")));
    }

    @Test
    void ignoresCaseOfCommandName() {
        registry.register(new PingCommand());

        assertEquals(help.execute(TestContext.of("ping")), help.execute(TestContext.of("PING")));
    }

    @Test
    void escapesDescriptionsInList() {
        String answer = help.execute(TestContext.of(""));

        assertTrue(answer.contains("help &lt;команда&gt;"));
    }

    @Test
    void separatesGroupsWithEmptyLine() {
        registry.register(new PingCommand());

        String answer = help.execute(TestContext.of(""));

        assertTrue(answer.contains("\n\n/ping"));
    }

    @Test
    void groupsGoInFixedOrderNotInRegistrationOrder() {
        registry.register(new PingCommand());
        registry.register(new StartCommand());

        String answer = help.execute(TestContext.of(""));

        assertTrue(answer.indexOf("/start") < answer.indexOf("/ping"));
    }

    @Test
    void emptyGroupsAddNoExtraLines() {
        String answer = help.execute(TestContext.of(""));

        assertFalse(answer.contains("\n\n\n"));
    }

    @Test
    void commandWithoutGroupGoesToOther() {
        Assertions.assertEquals(CommandGroup.OTHER, new PingCommand().group());
    }

    @Test
    void escapesUnknownCommandName() {
        String answer = help.execute(TestContext.of("<b>"));

        assertTrue(answer.contains("Нет команды «&lt;b&gt;»"));
    }
}
