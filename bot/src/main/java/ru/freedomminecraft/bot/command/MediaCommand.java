package ru.freedomminecraft.bot.command;

import java.util.List;
import ru.freedomminecraft.bot.format.Emoji;
import ru.freedomminecraft.bot.format.Html;
import ru.freedomminecraft.bot.serverinfo.StaffGroup;

public class MediaCommand extends StaffCommand {
    public MediaCommand(List<StaffGroup> media) {
        super(media);
    }

    @Override
    public String name() {
        return "media";
    }

    @Override
    public String description() {
        return "Стримеры и блогеры сервера";
    }

    @Override
    protected String header() {
        return Html.title(Emoji.TV, "МЕДИА FREEDOM") + "\n\n"
            + "Стримы и ролики про сервер — заходите и поддержите ребят!";
    }
}
