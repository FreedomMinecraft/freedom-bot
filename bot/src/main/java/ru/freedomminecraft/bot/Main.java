package ru.freedomminecraft.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.Executors;

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
import ru.freedomminecraft.bot.command.news.SubscribeCommand;
import ru.freedomminecraft.bot.config.BotConfig;
import ru.freedomminecraft.bot.config.DatabaseConfig;
import ru.freedomminecraft.bot.serverinfo.ServerInfo;
import ru.freedomminecraft.bot.serverinfo.ServerInfoSource;
import ru.freedomminecraft.bot.serverinfo.ServerInfoSources;
import ru.freedomminecraft.bot.subscription.InMemorySubscriptionRepository;
import ru.freedomminecraft.bot.subscription.JdbcSubscriptionRepository;
import ru.freedomminecraft.bot.subscription.SubscriptionRepository;
import ru.freedomminecraft.bot.subscription.SubscriptionMenu;
import ru.freedomminecraft.bot.telegram.TelegramBot;
import ru.freedomminecraft.bot.telegram.TelegramPostSender;
import ru.freedomminecraft.bot.news.LoggingNewsListener;
import ru.freedomminecraft.bot.news.NewsChannel;
import ru.freedomminecraft.bot.callback.CallbackDispatcher;
import ru.freedomminecraft.bot.broadcast.NewsBroadcaster;
import ru.freedomminecraft.bot.broadcast.Throttle;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws TelegramApiException, SQLException {
        BotConfig config = BotConfig.fromEnvironment(System.getenv());
        SubscriptionRepository subscriptions = openSubscriptions(config);

        ServerInfoSource source = ServerInfoSources.fromEnvironment(System.getenv());
        log.info("Данные о сервере: {}", source);
        ServerInfo info = source.load();
        SubscriptionMenu subscriptionMenu = new SubscriptionMenu(subscriptions);

        CommandRegistry registry = new CommandRegistry();
        registry.register(new StartCommand());
        registry.register(new HelpCommand(registry));
        registry.register(new AboutCommand());
        registry.register(new SubscribeCommand(subscriptionMenu));
        registry.register(new AdminsCommand(info.admins(), info.support()));
        registry.register(new EventorsCommand(info.eventors()));
        registry.register(new MediaCommand(info.media()));
        registry.register(new ChatsCommand(info.chats()));
        registry.register(new PingCommand());
        registry.register(new AuthorCommand());

        CommandDispatcher dispatcher = new CommandDispatcher(new CommandParser(), registry);
        CallbackDispatcher callbacks = new CallbackDispatcher();
        callbacks.register(subscriptionMenu);
        TelegramClient client = new OkHttpTelegramClient(config.token());
        Optional<NewsChannel> news = openNewsChannel(config, subscriptions, client);

        TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication();
        application.registerBot(config.token(), new TelegramBot(client, dispatcher, callbacks, news));

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
        return new JdbcSubscriptionRepository(connection);
    }

    private static Optional<NewsChannel> openNewsChannel(BotConfig config, SubscriptionRepository subscriptions, TelegramClient client){
        if (config.newsChannelId().isEmpty()){
            log.warn("NEWS_CHANNEL_ID не задан: посты канала бот не читает.");
            return Optional.empty();
        }
        long channelId = config.newsChannelId().get();
        NewsChannel channel = new NewsChannel(channelId);
        channel.subscribe(new LoggingNewsListener());
        channel.subscribe(new NewsBroadcaster(
            subscriptions,
            new TelegramPostSender(client),
            Throttle.every(NewsBroadcaster.TELEGRAM_PAUSE),
            Executors.newSingleThreadExecutor()));
        log.info("Бот слушает канал новостей: {}", channelId);
        return Optional.of(channel);
    }
}
