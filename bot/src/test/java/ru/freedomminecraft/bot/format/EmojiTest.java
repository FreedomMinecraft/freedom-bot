package ru.freedomminecraft.bot.format;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmojiTest {
    @Test
    void htmlIsTelegramCustomEmojiTag() {
        assertEquals("<tg-emoji emoji-id=\"5411394265924257943\">👋</tg-emoji>", Emoji.WAVE.html());
    }

    @Test
    void everyIdIsNumber() {
        for (Emoji emoji : Emoji.values()) {
            assertTrue(emoji.id().matches("\\d+"), "Неверный id у " + emoji);
        }
    }

    @Test
    void idsAreUnique() {
        Set<String> ids = new HashSet<>();
        for (Emoji emoji : Emoji.values()) {
            assertTrue(ids.add(emoji.id()), "Повторяется id у " + emoji);
        }
    }
}
