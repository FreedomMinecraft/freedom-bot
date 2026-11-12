package ru.freedomminecraft.bot.config;

import java.util.Map;

import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void readsDatabaseSettingsFromEnvironment(){
        BotConfig config = BotConfig.fromEnvironment(Map.of(
            "BOT_TOKEN", "123:abc",
            "DB_URL", "jdbc:postgresql://localhost/bot",
            "DB_USER", "bot",
            "DB_PASSWORD", "secret" ));

        DatabaseConfig database = config.database().orElseThrow();
        assertEquals("jdbc:postgresql://localhost/bot", database.url());
        assertEquals("bot", database.user());
        assertEquals("secret", database.password());
    }

    @Test
    void sqliteNeedsOnlyUrl(){
        BotConfig config = BotConfig.fromEnvironment(Map.of(
            "BOT_TOKEN", "123:abc",
            "DB_URL", "jdbc:sqlite:/app/data/bot.db"));

        DatabaseConfig database = config.database().orElseThrow();
        assertEquals("jdbc:sqlite:/app/data/bot.db", database.url());
        assertNull(database.user());
        assertNull(database.password());
    }

    @Test
    void databaseIsEmptyWhenUrlIsMissing(){
        BotConfig config = BotConfig.fromEnvironment(Map.of("BOT_TOKEN", "123:abc"));

        assertTrue(config.database().isEmpty());
    }

    @Test
    void databaseIsEmptyWhenUrlIsBlank(){
        BotConfig config = BotConfig.fromEnvironment(Map.of(
            "BOT_TOKEN", "123:abc",
            "DB_URL", "   "));

        assertTrue(config.database().isEmpty());
    }

    @Test
    void doesNotShowDatabasePasswordInToString(){
        BotConfig config = BotConfig.fromEnvironment(Map.of(
            "BOT_TOKEN", "123:abc",
            "DB_URL", "jdbc:sqlite:/app/data/bot.db",
            "DB_PASSWORD", "secret"));

        assertFalse(config.toString().contains("secret"));
    }
}
