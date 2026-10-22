package ru.freedomminecraft.bot.serverinfo;

import java.util.Objects;

public record Link(String title, String url) {
    public Link {
        Objects.requireNonNull(title, "У ссылки нет title");
        Objects.requireNonNull(url, "У ссылки нет url");
    }
}
