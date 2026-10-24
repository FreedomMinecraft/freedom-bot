package ru.freedomminecraft.bot.serverinfo;

import java.util.List;
import java.util.Objects;

public record ServerInfo(
        List<StaffGroup> admins,
        List<StaffGroup> eventors,
        List<StaffGroup> media,
        List<Chat> chats,
        List<Link> support) {
    public ServerInfo {
        admins = List.copyOf(Objects.requireNonNull(admins, "В данных нет раздела admins"));
        eventors = List.copyOf(Objects.requireNonNull(eventors, "В данных нет раздела eventors"));
        media = List.copyOf(Objects.requireNonNull(media, "В данных нет раздела media"));
        chats = List.copyOf(Objects.requireNonNull(chats, "В данных нет раздела chats"));
        support = List.copyOf(Objects.requireNonNull(support, "В данных нет раздела support"));
    }
}
