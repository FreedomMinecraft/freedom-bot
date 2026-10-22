package ru.freedomminecraft.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import ru.freedomminecraft.bot.command.*;
import ru.freedomminecraft.bot.config.BotConfig;
import ru.freedomminecraft.bot.serverinfo.JsonServerInfoSource;
import ru.freedomminecraft.bot.serverinfo.ServerInfo;
import ru.freedomminecraft.bot.telegram.TelegramBot;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws TelegramApiException {
        BotConfig config = BotConfig.fromEnvironment(System.getenv());

        CommandRegistry registry = new CommandRegistry();
        registry.register(new HelpCommand(registry));
        registry.register(new AboutCommand());
        registry.register(new AuthorCommand());
        registry.register(new PingCommand());
        registry.register(new StartCommand());
        ServerInfo info = new JsonServerInfoSource(JsonServerInfoSource.DEFAULT_RESOURCE).load();
        registry.register(new StaffCommand("admins", "Админ-состав сервера", "Админ-состав FREEDOM:", info.admins()));
        registry.register(new ChatsCommand(info.chats()));
        registry.register(new StaffCommand("eventors", "Ивенторы сервера", "Ивенторы FREEDOM:", info.eventors()));
        registry.register(new StaffCommand("media", "Медиа-состав сервера", "Медиа-состав FREEDOM:", info.media()));

        CommandDispatcher dispatcher = new CommandDispatcher(new CommandParser(), registry);
        TelegramClient client = new OkHttpTelegramClient(config.token());

        TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication();
        application.registerBot(config.token(), new TelegramBot(client, dispatcher));

        log.info("Бот запущен.");
    }
}
