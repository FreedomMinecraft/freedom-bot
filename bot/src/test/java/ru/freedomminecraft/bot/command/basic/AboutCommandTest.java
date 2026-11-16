package ru.freedomminecraft.bot.command.basic;

import org.junit.jupiter.api.Test;
import ru.freedomminecraft.bot.command.TestContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AboutCommandTest {
    private final AboutCommand about = new AboutCommand();

    @Test
    void nameIsAbout(){
        assertEquals("about", about.name());
    }

    @Test
    void mentionServerName(){
        String answer = about.execute(TestContext.of("")).text();

        assertTrue(answer.contains("FREEDOM"));
    }

    @Test
    void pointsToHelp(){
        String answer = about.execute(TestContext.of("")).text();

        assertTrue(answer.contains("/help"));
    }

    @Test
    void serverAddressCanBeCopied(){
        String answer = about.execute(TestContext.of("")).text();

        assertTrue(answer.contains("<code>freedom.servop.ru</code>"));
    }
}
