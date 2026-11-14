package ru.freedomminecraft.bot.command.basic;

import org.junit.jupiter.api.Test;
import ru.freedomminecraft.bot.command.CommandRegistry;
import ru.freedomminecraft.bot.command.TestContext;

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
        String answer = start.execute(TestContext.of("")).text();

        assertTrue(answer.contains("FREEDOM"));
    }

    @Test
    void pointsToHelp(){
        String answer = start.execute(TestContext.of("")).text();

        assertTrue(answer.contains("/help"));
    }

    @Test
    void usesPremiumEmoji(){
        String answer = start.execute(TestContext.of("")).text();

        assertTrue(answer.contains("<tg-emoji"));
    }

    @Test
    void appearsInHelpAutomatically(){
        CommandRegistry registry = new CommandRegistry();
        registry.register(new HelpCommand(registry));
        registry.register(start);

        String helpAnswer = registry.find("help").get().execute(TestContext.of("")).text();

        assertTrue(helpAnswer.contains("/start — Начать работу с ботом"));
    }
}
