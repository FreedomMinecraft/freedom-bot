package ru.freedomminecraft.bot.command;

import org.junit.jupiter.api.Test;

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
        String answer = about.execute("");

        assertTrue(answer.contains("FREEDOM"));
    }

    @Test
    void pointsToHelp(){
        String answer = about.execute("");

        assertTrue(answer.contains("/help"));
    }
}
