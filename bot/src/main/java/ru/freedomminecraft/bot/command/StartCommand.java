package ru.freedomminecraft.bot.command;

import ru.freedomminecraft.bot.format.Html;
import ru.freedomminecraft.bot.format.Emoji;

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
    public CommandGroup group(){
        return CommandGroup.BASIC;
    }

    @Override
    public String execute(String argument){
        return Html.title(Emoji.WAVE, "ДОБРО ПОЖАЛОВАТЬ НА FREEDOM!") + "\n\n"
            + "Я бот сервера — со мной вы не пропустите главное:\n\n"
            + Emoji.COMPASS.html() + " • Путеводитель: администрация, ивенторы, медиа и чаты\n"
            + Emoji.BELL.html() + " • Новости по подписке: ивенты, розыгрыши, важное (скоро)\n"
            + Emoji.SHIELD.html() + " • Защита игрового аккаунта через Telegram (скоро)\n\n"
            + Emoji.BOOK.html() + " Все команды — /help";
    }
}
