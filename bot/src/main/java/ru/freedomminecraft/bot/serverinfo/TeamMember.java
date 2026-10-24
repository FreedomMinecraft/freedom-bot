package ru.freedomminecraft.bot.serverinfo;

import java.util.List;
import java.util.Objects;

public record TeamMember(String nickname, List<Link> links) {
    public TeamMember {
        Objects.requireNonNull(nickname, "У участника нет nickname");
        links = links == null ? List.of() : List.copyOf(links);
    }
}
