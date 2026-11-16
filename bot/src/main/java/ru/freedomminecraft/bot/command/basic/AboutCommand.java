package ru.freedomminecraft.bot.command.basic;

import ru.freedomminecraft.bot.command.Command;
import ru.freedomminecraft.bot.command.CommandContext;
import ru.freedomminecraft.bot.command.CommandGroup;
import ru.freedomminecraft.bot.format.Emoji;
import ru.freedomminecraft.bot.format.Html;
import ru.freedomminecraft.bot.reply.Reply;

public class AboutCommand implements Command {
    @Override
    public String name(){
        return "about";
    }

    @Override
    public String description(){
        return "Что умеет бот";
    }

    @Override
    public CommandGroup group () {
        return CommandGroup.BASIC;
    }

    @Override
    public Reply execute(CommandContext context){
        return Reply.of(Html.title(Emoji.ROBOT, "FREEDOM BOT") + "\n\n"
            + "Бот Minecraft-сервера FREEDOM.\n\n"
            + Emoji.COMPASS.html() + " • Путеводитель по серверу: кто в администрации, где общаться, чьи стримы смотреть\n"
            + Emoji.BELL.html() + " • Адресные уведомления — только то, на что подписались (скоро)\n"
            + Emoji.SHIELD.html() + " • Привязка игрового аккаунта к Telegram для защиты (скоро)\n\n"
            + Emoji.FLAG.html() + " • IP: " + Html.code("freedom.servop.ru") + "\n"
            + Emoji.GAMEPAD.html() + " • Версия: 1.21.10\n"
            + Emoji.GLOBE.html() + " • Сайт: " + Html.link("freedom-minecraft.ru", "https://freedom-minecraft.ru/") + "\n\n"
            + Emoji.BOOK.html() + " Все команды — /help");
    }
}
