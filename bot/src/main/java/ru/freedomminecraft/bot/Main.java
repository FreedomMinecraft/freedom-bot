package ru.freedomminecraft.bot;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import ru.freedomminecraft.bot.command.*;
import ru.freedomminecraft.bot.config.BotConfig;
import ru.freedomminecraft.bot.telegram.TelegramBot;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        BotConfig config = BotConfig.fromEnvironment(System.getenv());

        CommandRegistry registry = new CommandRegistry();
        registry.register(new HelpCommand(registry));
        registry.register(new AboutCommand());
        registry.register(new AuthorCommand());
        registry.register(new PingCommand());

        CommandDispatcher dispatcher = new CommandDispatcher(new CommandParser(), registry);
        TelegramClient client = new OkHttpTelegramClient(config.token());

        TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication();
        application.registerBot(config.token(), new TelegramBot(client, dispatcher));

        System.out.println("Бот запущен.");
    }
}
