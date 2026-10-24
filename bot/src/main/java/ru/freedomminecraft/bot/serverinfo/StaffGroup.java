package ru.freedomminecraft.bot.serverinfo;

import java.util.List;
import java.util.Objects;

public record StaffGroup(String title, List<TeamMember> members) {
    public StaffGroup {
        Objects.requireNonNull(title, "У группы нет title");
        members = List.copyOf(Objects.requireNonNull(members, "В группе " + title + " нет members"));
    }
}
