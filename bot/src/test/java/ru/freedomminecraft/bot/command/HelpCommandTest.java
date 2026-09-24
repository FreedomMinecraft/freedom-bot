package ru.freedomminecraft.bot.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        String answer = help.execute("");

        assertTrue(answer.contains("/ping"));
        assertTrue(answer.contains("Проверить, что бот на связи"));
    }

    @Test
    void nullArgumentWorksLikeEmpty(){
        registry.register(new PingCommand());

        assertEquals(help.execute(""), help.execute(null));
    }

    @Test
    void showsHelpForOneCommand(){
        registry.register(new PingCommand());

        String answer = help.execute("ping");

        assertEquals("/ping - Проверить, что бот на связи", answer);
    }

    @Test
    void ignoresSpacesAroundCommandName(){
        registry.register(new PingCommand());

        assertEquals(help.execute("ping"), help.execute(" ping "));
    }

    @Test
    void reportsUnknownCommand(){
        String answer = help.execute("fly");

        assertTrue(answer.contains("Нет такой команды: fly"));
    }

    @Test
    void acceptsCommandNameWithSlash() {
        registry.register(new PingCommand());

        assertEquals(help.execute("ping"), help.execute("/ping"));
    }

    @Test
    void ignoresCaseOfCommandName() {
        registry.register(new PingCommand());

        assertEquals(help.execute("ping"), help.execute("PING"));
    }
}
