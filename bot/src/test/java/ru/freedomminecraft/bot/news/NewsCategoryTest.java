package ru.freedomminecraft.bot.news;

import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NewsCategoryTest {
    @Test
    void findsEachCategoryByHashtag(){
        assertEquals(Set.of(NewsCategory.IMPORTANT), NewsCategory.fromText("Техработы в 18:00 #важное"));
        assertEquals(Set.of(NewsCategory.EVENT), NewsCategory.fromText("Завтра PvP турнир #ивент"));
        assertEquals(Set.of(NewsCategory.GIVEAWAY), NewsCategory.fromText("Дарим баны #розыгрыш"));
    }

    @Test
    void findsSeveralCategoriesInOnePost(){
        Set<NewsCategory> categories = NewsCategory.fromText("Ивент с призами #ивент #розыгрыш");

        assertEquals(Set.of(NewsCategory.EVENT, NewsCategory.GIVEAWAY), categories);
    }

    @Test
    void ignoresLetterCase(){
        assertEquals(Set.of(NewsCategory.IMPORTANT), NewsCategory.fromText("#ВАЖНОЕ обновление"));
    }

    @Test
    void hashtagMayBeFollowedByPunctuation(){
        assertEquals(Set.of(NewsCategory.EVENT), NewsCategory.fromText("Все на #ивент!"));
    }

    @Test
    void hashtagsWrittenTogetherAreFound(){
        Set<NewsCategory> categories = NewsCategory.fromText("#ивент#розыгрыш");

        assertEquals(Set.of(NewsCategory.EVENT, NewsCategory.GIVEAWAY), categories);
    }

    @Test
    void similarHashtagIsNotCategory(){
        assertTrue(NewsCategory.fromText("Итоги #ивенты прошлой недели").isEmpty());
    }

    @Test
    void wordWithoutHashSignIsNotCategory(){
        assertTrue(NewsCategory.fromText("Это важное объявление по поводу ивента").isEmpty());
    }

    @Test
    void postWithoutTextHasNoCategories(){
        assertTrue(NewsCategory.fromText(null).isEmpty());
        assertTrue(NewsCategory.fromText("").isEmpty());
    }

    @Test
    void resultCannotBeChanged(){
        Set<NewsCategory> categories = NewsCategory.fromText("#ивент");
        assertThrows(UnsupportedOperationException.class, () -> categories.add(NewsCategory.IMPORTANT));
    }
}
