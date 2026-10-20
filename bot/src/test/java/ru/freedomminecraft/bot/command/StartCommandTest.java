package ru.freedomminecraft.bot.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StartCommandTest {
    private final StartCommand start = new StartCommand();

    @Test
    void nameIsStart(){
        assertEquals("start", start.name());
    }

    @Test
    void mentionServerName(){
        String answer = start.execute("");

        assertTrue(answer.contains("FREEDOM"));
    }

    @Test
    void pointsToHelp(){
        String answer = start.execute("");

        assertTrue(answer.contains("/help"));
    }

    @Test
    void appearsInHelpAutomatically(){
        CommandRegistry registry = new CommandRegistry();
        registry.register(new HelpCommand(registry));
        registry.register(start);

        String helpAnswer = registry.find("help").get().execute("");

        assertTrue(helpAnswer.contains("/start - начать работу с ботом"));
    }
}
