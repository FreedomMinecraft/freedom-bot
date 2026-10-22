package ru.freedomminecraft.bot.serverinfo;

import java.util.List;
import java.util.Objects;

public record TeamMember(String nickname, String position, List<Link> links) {
    public TeamMember {
        Objects.requireNonNull(nickname, "У участника нет nickname");
        Objects.requireNonNull(position, "У участника " + nickname + " нет position");
        links = links == null ? List.of() : List.copyOf(links);
    }
}
