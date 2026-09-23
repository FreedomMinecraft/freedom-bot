package ru.freedomminecraft.bot.config;

import java.util.Map;

public record BotConfig(String token) {

    public static final String TOKEN_VARIABLE = "BOT_TOKEN";

    public static BotConfig fromEnvironment(Map<String, String> environment) {
        String token = environment.get(TOKEN_VARIABLE);
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Не задан токен бота: установите переменную окружения " + TOKEN_VARIABLE);
        }
        return new BotConfig(token.strip());
    }

    @Override
    public String toString() {
        return "BotConfig[token=***]";
    }
}
