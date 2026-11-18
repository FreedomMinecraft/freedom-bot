package ru.freedomminecraft.bot.command.news;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.freedomminecraft.bot.command.CommandRegistry;
import ru.freedomminecraft.bot.command.TestContext;
import ru.freedomminecraft.bot.command.basic.HelpCommand;
import ru.freedomminecraft.bot.news.NewsCategory;
import ru.freedomminecraft.bot.reply.Reply;
import ru.freedomminecraft.bot.subscription.InMemorySubscriptionRepository;
import ru.freedomminecraft.bot.subscription.SubscriptionMenu;
import ru.freedomminecraft.bot.subscription.SubscriptionRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubscribeCommandTest {

    private SubscriptionRepository subscriptions;
    private SubscribeCommand subscribe;

    @BeforeEach
    void setUp(){
        subscriptions = new InMemorySubscriptionRepository();
        subscribe = new SubscribeCommand(new SubscriptionMenu(subscriptions));
    }

    @Test
    void nameIsSubscribe(){
        assertEquals("subscribe", subscribe.name());
    }

    @Test
    void repliesWithKeyboard(){
        Reply reply = subscribe.execute(TestContext.of(""));

        assertTrue(reply.keyboard().isPresent());
    }

    @Test
    void showsSubscriptionsOfUserWhoAsked(){
        subscriptions.subscribe(TestContext.USER_ID, NewsCategory.IMPORTANT);

        Reply reply = subscribe.execute(TestContext.of(""));

        String firstButton = reply.keyboard().orElseThrow().rows().get(0).get(0).text();
        assertTrue(firstButton.startsWith("✅"));
    }

    @Test
    void appearsInHelpAutomatically(){
        CommandRegistry registry = new CommandRegistry();
        registry.register(new HelpCommand(registry));
        registry.register(subscribe);

        String help = registry.find("help").orElseThrow().execute(TestContext.of("")).text();
        assertTrue(help.contains("/subscribe — Выбрать, какие новости присылать"));
    }
}
