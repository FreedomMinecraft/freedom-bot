package ru.freedomminecraft.bot.command.guide;

import java.util.List;
import java.util.StringJoiner;

import ru.freedomminecraft.bot.command.Command;
import ru.freedomminecraft.bot.command.CommandGroup;
import ru.freedomminecraft.bot.format.Html;
import ru.freedomminecraft.bot.serverinfo.Link;
import ru.freedomminecraft.bot.serverinfo.StaffGroup;
import ru.freedomminecraft.bot.serverinfo.TeamMember;

/**
 * Общая основа команд со списком людей по группам: /admins, /eventors, /media.
 * Порядок ответа задан здесь: заголовок, группы, подвал.
 * Наследник решает только, какие у него заголовок и подвал.
 */
public abstract class StaffCommand implements Command {
    private final List<StaffGroup> groups;

    protected StaffCommand(List<StaffGroup> groups) {
        this.groups = List.copyOf(groups);
    }

    /** Начало ответа в HTML. */
    protected abstract String header();

    /** Конец ответа в HTML. По умолчанию пусто. */
    protected String footer() {
        return "";
    }

    @Override
    public CommandGroup group() {
        return CommandGroup.GUIDE;
    }

    @Override
    public final String execute(String argument) {
        StringBuilder text = new StringBuilder(header()).append("\n\n");
        if (groups.isEmpty()) {
            text.append("Список пока пуст.\n");
        }
        for (StaffGroup group : groups) {
            text.append(Html.bold(group.title())).append("\n");
            for (TeamMember member : group.members()) {
                text.append(Html.BULLET).append(Html.escape(member.nickname()));
                if (!member.links().isEmpty()) {
                    text.append(" — ").append(links(member.links()));
                }
                text.append("\n");
            }
            text.append("\n");
        }
        text.append(footer());
        return text.toString().strip();
    }

    private static String links(List<Link> links) {
        StringJoiner joiner = new StringJoiner(" · ");
        for (Link link : links) {
            joiner.add(Html.link(link.title(), link.url()));
        }
        return joiner.toString();
    }
}
