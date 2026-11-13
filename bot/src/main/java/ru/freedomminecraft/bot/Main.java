package ru.freedomminecraft.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import ru.freedomminecraft.bot.command.*;
import ru.freedomminecraft.bot.command.basic.*;
import ru.freedomminecraft.bot.command.guide.AdminsCommand;
import ru.freedomminecraft.bot.command.guide.ChatsCommand;
import ru.freedomminecraft.bot.command.guide.EventorsCommand;
import ru.freedomminecraft.bot.command.guide.MediaCommand;
import ru.freedomminecraft.bot.config.BotConfig;
import ru.freedomminecraft.bot.config.DatabaseConfig;
import ru.freedomminecraft.bot.serverinfo.ServerInfo;
import ru.freedomminecraft.bot.serverinfo.ServerInfoSource;
import ru.freedomminecraft.bot.serverinfo.ServerInfoSources;
import ru.freedomminecraft.bot.subscription.InMemorySubscriptionRepository;
import ru.freedomminecraft.bot.subscription.JdbcSubscriptionsRepository;
import ru.freedomminecraft.bot.subscription.SubscriptionRepository;
import ru.freedomminecraft.bot.telegram.TelegramBot;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws TelegramApiException, SQLException {
        BotConfig config = BotConfig.fromEnvironment(System.getenv());
        SubscriptionRepository subscriptions = openSubscriptions(config);

        ServerInfoSource source = ServerInfoSources.fromEnvironment(System.getenv());
        log.info("Данные о сервере: {}", source);
        ServerInfo info = source.load();

        CommandRegistry registry = new CommandRegistry();
        registry.register(new StartCommand());
        registry.register(new HelpCommand(registry));
        registry.register(new AboutCommand());
        registry.register(new AdminsCommand(info.admins(), info.support()));
        registry.register(new EventorsCommand(info.eventors()));
        registry.register(new MediaCommand(info.media()));
        registry.register(new ChatsCommand(info.chats()));
        registry.register(new PingCommand());
        registry.register(new AuthorCommand());

        CommandDispatcher dispatcher = new CommandDispatcher(new CommandParser(), registry);
        TelegramClient client = new OkHttpTelegramClient(config.token());

        TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication();
        application.registerBot(config.token(), new TelegramBot(client, dispatcher));

        log.info("Бот запущен.");
    }

    private static SubscriptionRepository openSubscriptions(BotConfig config) throws SQLException {
        if (config.database().isEmpty()) {
            log.warn("DB_URL не задан: подписки хранятся в памяти и пропадут при перезапуске.");
            return new InMemorySubscriptionRepository();
        }
        DatabaseConfig database = config.database().get();
        Connection connection = DriverManager.getConnection(database.url(), database.user(), database.password());
        log.info("Подписки хранятся в базе: {}", database.url());
        return new JdbcSubscriptionsRepository(connection);
    }
}
