package ru.freedomminecraft.bot.command.basic;

import ru.freedomminecraft.bot.command.Command;
import ru.freedomminecraft.bot.command.CommandContext;
import ru.freedomminecraft.bot.format.Emoji;
import ru.freedomminecraft.bot.format.Html;
import ru.freedomminecraft.bot.reply.Reply;

public class AuthorCommand implements Command {
    @Override
    public String name() { return "author"; }

    @Override
    public String description() { return "Информация об авторах бота";}

    @Override
    public Reply execute(CommandContext context){
        return Reply.of(Html.title(Emoji.STAR, "АВТОРЫ БОТА") + "\n\n"
            + Html.BULLET + "Рещиков Андрей — @yuknowni\n"
            + Html.BULLET + "Потапов Вадим — @ivyxide");
    }
}
