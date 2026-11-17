package ru.freedomminecraft.bot.config;

import java.util.Map;
import java.util.Optional;

public record BotConfig(String token, Optional<DatabaseConfig> database, Optional<Long> newsChannelId) {

    public static final String TOKEN_VARIABLE = "BOT_TOKEN";
    public static final String NEWS_CHANNEL_VARIABLE = "NEWS_CHANNEL_ID";

    public static BotConfig fromEnvironment(Map<String, String> environment) {
        String token = environment.get(TOKEN_VARIABLE);
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Не задан токен бота: установите переменную окружения " + TOKEN_VARIABLE);
        }
        return new BotConfig(token.strip(), DatabaseConfig.fromEnvironment(environment),
            readNewsChannelId(environment));
    }

    private static Optional<Long> readNewsChannelId(Map<String, String> environment){
        String value = environment.get(NEWS_CHANNEL_VARIABLE);
        if (value == null || value.isBlank()){
            return Optional.empty();
        }
        try{
            return Optional.of(Long.parseLong(value.strip()));
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                "Переменная " + NEWS_CHANNEL_VARIABLE + " должна быть числом, а не: " + value);
        }
    }

    @Override
    public String toString() {
        return "BotConfig[token=***, database=" + database + ", newsChannelId=" + newsChannelId + "]";
    }
}
