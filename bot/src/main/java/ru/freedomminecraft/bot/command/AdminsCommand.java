package ru.freedomminecraft.bot.command;

import java.util.List;
import ru.freedomminecraft.bot.format.Emoji;
import ru.freedomminecraft.bot.format.Html;
import ru.freedomminecraft.bot.serverinfo.Link;
import ru.freedomminecraft.bot.serverinfo.StaffGroup;

public class AdminsCommand extends StaffCommand {
    private final List<Link> support;

    public AdminsCommand(List<StaffGroup> admins, List<Link> support) {
        super(admins);
        this.support = List.copyOf(support);
    }

    @Override
    public String name() {
        return "admins";
    }

    @Override
    public String description() {
        return "Администрация и поддержка";
    }

    @Override
    protected String header() {
        return Html.title(Emoji.CROWN, "АДМИНИСТРАЦИЯ FREEDOM");
    }

    @Override
    protected String footer() {
        if (support.isEmpty()) {
            return "";
        }
        StringBuilder text = new StringBuilder(Emoji.SOS.html())
            .append(" ")
            .append(Html.bold("Нужна помощь?"))
            .append(" Пишите в поддержку:\n");
        for (Link link : support) {
            text.append(Html.BULLET)
                .append(Html.link(link.title(), link.url()))
                .append("\n");
        }
        return text.toString();
    }
}
