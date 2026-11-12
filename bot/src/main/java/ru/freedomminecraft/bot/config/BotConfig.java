package ru.freedomminecraft.bot.config;

import java.util.Map;
import java.util.Optional;

public record BotConfig(String token, Optional<DatabaseConfig> database) {

    public static final String TOKEN_VARIABLE = "BOT_TOKEN";

    public static BotConfig fromEnvironment(Map<String, String> environment) {
        String token = environment.get(TOKEN_VARIABLE);
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Не задан токен бота: установите переменную окружения " + TOKEN_VARIABLE);
        }
        return new BotConfig(token.strip(), DatabaseConfig.fromEnvironment(environment));
    }

    @Override
    public String toString() {
        return "BotConfig[token=***, database=" + database + "]";
    }
}
