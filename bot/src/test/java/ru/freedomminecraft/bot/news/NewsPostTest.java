package ru.freedomminecraft.bot.news;

import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewsPostTest {

    @Test
    void categoriesComeFromText(){
        NewsPost post = new NewsPost(-1337228, 7, "Розыгрыш на ивенте! #ивент #розыгрыш");

        assertEquals(Set.of(NewsCategory.EVENT, NewsCategory.GIVEAWAY), post.categories());
    }
}
