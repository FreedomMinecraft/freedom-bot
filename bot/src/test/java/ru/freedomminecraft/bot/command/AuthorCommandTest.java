package ru.freedomminecraft.bot.command;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthorCommandTest {
    private final AuthorCommand author = new AuthorCommand();

    @Test
    void nameIsAuthor() {
        assertEquals("author", author.name());
    }

    @Test
    void mentionBothAuthors(){
        String answer = author.execute("");

        assertTrue(answer.contains("@yuknowni"));
        assertTrue(answer.contains("@ivyxide"));
    }
}
