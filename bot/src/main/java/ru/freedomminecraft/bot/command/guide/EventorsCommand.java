package ru.freedomminecraft.bot.command.guide;

import java.util.List;
import ru.freedomminecraft.bot.format.Emoji;
import ru.freedomminecraft.bot.format.Html;
import ru.freedomminecraft.bot.serverinfo.StaffGroup;

public class EventorsCommand extends StaffCommand {
    public EventorsCommand(List<StaffGroup> eventors) {
        super(eventors);
    }

    @Override
    public String name() {
        return "eventors";
    }

    @Override
    public String description() {
        return "Ивенторы сервера";
    }

    @Override
    protected String header() {
        return Html.title(Emoji.PARTY, "ИВЕНТОРЫ FREEDOM") + "\n\n"
            + "Придумывают и проводят ивенты сервера.";
    }
}
