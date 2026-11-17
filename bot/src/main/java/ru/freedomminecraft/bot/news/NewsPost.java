package ru.freedomminecraft.bot.news;

import java.util.Set;

public record NewsPost(long channelId, int messageId, String text) {
    public Set<NewsCategory> categories() {
        return NewsCategory.fromText(text);
    }
}
