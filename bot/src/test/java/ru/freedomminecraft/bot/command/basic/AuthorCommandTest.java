package ru.freedomminecraft.bot.command.basic;

import org.junit.jupiter.api.Test;
import ru.freedomminecraft.bot.command.TestContext;


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
        String answer = author.execute(TestContext.of("")).text();

        assertTrue(answer.contains("@yuknowni"));
        assertTrue(answer.contains("@ivyxide"));
    }
}
