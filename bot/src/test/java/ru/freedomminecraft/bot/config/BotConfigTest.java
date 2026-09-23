package ru.freedomminecraft.bot.config;

import java.util.Map;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BotConfigTest {

    @Test
    void readsTokenFromEnvironment() {
        BotConfig config = BotConfig.fromEnvironment(Map.of("BOT_TOKEN", "123:abc"));

        assertEquals("123:abc", config.token());
    }

    @Test
    void stripsSpacesAroundToken() {
        BotConfig config = BotConfig.fromEnvironment(Map.of("BOT_TOKEN", "   123:abc   "));

        assertEquals("123:abc", config.token());
    }

    @Test
    void failsWhenTokenIsMissing() {
        assertThrows(IllegalStateException.class, () -> BotConfig.fromEnvironment(Map.of()));
    }

    @Test
    void failsWhenTokenIsBlank() {
        assertThrows(IllegalStateException.class, () -> BotConfig.fromEnvironment(Map.of("BOT_TOKEN", "   ")));
    }

    @Test
    void doesNotShowTokenInToString() {
        BotConfig config = BotConfig.fromEnvironment(Map.of("BOT_TOKEN", "123:abc"));

        assertFalse(config.toString().contains("123:abc"));
    }
}
