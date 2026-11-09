package ru.freedomminecraft.bot.command.basic;

import ru.freedomminecraft.bot.command.Command;
import ru.freedomminecraft.bot.format.Emoji;
import ru.freedomminecraft.bot.format.Html;

public class AuthorCommand implements Command {
    @Override
    public String name() { return "author"; }

    @Override
    public String description() { return "Информация об авторах бота";}

    @Override
    public String execute(String argument){
        return Html.title(Emoji.STAR, "АВТОРЫ БОТА") + "\n\n"
            + Html.BULLET + "Рещиков Андрей — @yuknowni\n"
            + Html.BULLET + "Потапов Вадим — @ivyxide";
    }
}
