package ru.freedomminecraft.bot.command;

public class StartCommand implements Command{
    @Override
    public String name() {
        return "start";
    }

    @Override
    public String description(){
        return "Начать работу с ботом";
    }

    @Override
    public String execute(String argument){
        return "Это бот Minecraft-сервера FREEDOM.\n"
            + "Здесь можно защитить игровой аккаунт, получить важные новости сервера "
            + "и узнать все о расах, кланах и админах.\n"
            + "Список команд: /help\n"
            + "Подробнее о боте: /about";
    }
}
