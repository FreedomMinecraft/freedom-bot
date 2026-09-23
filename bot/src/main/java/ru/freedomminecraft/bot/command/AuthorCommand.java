package ru.freedomminecraft.bot.command;

public class AuthorCommand implements Command {
    @Override
    public String name() { return "author"; }

    @Override
    public String description() { return "информация об авторах бота";}

    @Override
    public String execute(String argument){
        return "Авторы Freedom Bot:\n"
            + "Рещиков Андрей - @yuknowni\n"
            + "Потапов Вадим - @ivyxide";
    }
}
