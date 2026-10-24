package ru.freedomminecraft.bot.format;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HtmlTest {
    @Test
    void escapesAngleBrackets(){
        assertEquals("help &lt;команда&gt;", Html.escape("help <команда>"));
    }

    @Test
    void escapesAmpersand(){
        assertEquals("Чат &amp; новости", Html.escape("Чат & новости"));
    }
    @Test
    void escapesAmpersandOnlyOnce(){
        assertEquals("&amp;lt;", Html.escape("&lt;"));
    }
    @Test
    void leavesPlainTextAsIs(){
        assertEquals("_CraftWoker_ - Модератор", Html.escape("_CraftWoker_ - Модератор"));
    }
}
