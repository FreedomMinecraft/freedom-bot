package ru.freedomminecraft.bot.subscription;

import java.util.Set;

import ru.freedomminecraft.bot.news.NewsCategory;

/**
 * Хранилище подписок: кто на какие новости подписан.
 * Пользователь = Telegram ID;
 */
public interface SubscriptionRepository {
    /** Подписать пользователя на категорию. */
    void subscribe(long userId, NewsCategory category);
    /** Отписать пользователя от категории. */
    void unsubscribe(long userId, NewsCategory category);
    /** Подписки пользователя. */
    Set<NewsCategory> categoriesOf(long userId);
    /** Подписанные на категорию. */
    Set<Long> subscribersOf(NewsCategory category);
}
