package ru.freedomminecraft.bot.serverinfo;

import java.util.Objects;

public record Chat(String name, String description, String url) {
    public Chat {
        Objects.requireNonNull(name, "У чата нет name");
        Objects.requireNonNull(description, "У чата " + name + " нет description");
        Objects.requireNonNull(url, "У чата " + name + " нет url");
    }
}
