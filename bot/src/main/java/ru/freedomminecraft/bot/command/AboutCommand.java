package ru.freedomminecraft.bot.command;

public class AboutCommand implements Command{
    @Override
    public String name(){
        return "about";
    }

    @Override
    public String description(){
        return "Назначение бота";
    }

    @Override
    public String execute(String argument){
        return "Freedom Bot - телеграм-бот Minecraft-сервера FREEDOM.\n"
            + "Он поможет защитить игровой аккаунт двухфакторным подтверждением, "
            + "присылает самые важные новости сервера "
            + "и работает как путеводитель: расы, кланы, админ-состав и чаты.\n"
            + "Список команд: /help";
    }
}
