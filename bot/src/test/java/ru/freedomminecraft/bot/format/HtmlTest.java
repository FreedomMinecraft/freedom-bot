package ru.freedomminecraft.bot.format;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void boldEscapesText() {
        assertEquals("<b>Модераторы &amp; хелперы</b>", Html.bold("Модераторы & хелперы"));
    }

    @Test
    void codeWrapsText() {
        assertEquals("<code>freedom.servop.ru</code>", Html.code("freedom.servop.ru"));
    }

    @Test
    void linkShowsTextAndHidesUrl() {
        assertEquals("<a href=\"https://t.me/test\">Канал</a>", Html.link("Канал", "https://t.me/test"));
    }

    @Test
    void linkEscapesUrl() {
        String link = Html.link("Сайт", "https://example.com/?a=1&b=\"2\"");

        assertTrue(link.contains("href=\"https://example.com/?a=1&amp;b=&quot;2&quot;\""));
    }

    @Test
    void titleIsBoldBetweenTwoEmoji() {
        String emoji = Emoji.BOOK.html();

        assertEquals(emoji + " <b>КОМАНДЫ</b> " + emoji, Html.title(Emoji.BOOK, "КОМАНДЫ"));
    }
}
