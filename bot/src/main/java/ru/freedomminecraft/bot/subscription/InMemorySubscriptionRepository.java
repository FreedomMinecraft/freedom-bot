package ru.freedomminecraft.bot.subscription;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ru.freedomminecraft.bot.news.NewsCategory;

public class InMemorySubscriptionRepository implements SubscriptionRepository{
    private final Map<Long, Set<NewsCategory>> categoriesByUser = new HashMap<>();

    @Override
    public synchronized void subscribe(long userId, NewsCategory category) {
        categoriesByUser
            .computeIfAbsent(userId, id -> EnumSet.noneOf(NewsCategory.class))
            .add(category);
    }

    @Override
    public synchronized void unsubscribe(long userId, NewsCategory category) {
        Set<NewsCategory> categories = categoriesByUser.get(userId);
        if (categories != null) {
            categories.remove(category);
        }
    }

    @Override
    public synchronized Set<NewsCategory> categoriesOf(long userId) {
        return Set.copyOf(categoriesByUser.getOrDefault(userId,Set.of()));
    }

    @Override
    public synchronized Set<Long> subscribersOf(NewsCategory category) {
        Set<Long> subscribers = new HashSet<>();
        for (Map.Entry<Long, Set<NewsCategory>> entry : categoriesByUser.entrySet()) {
            if (entry.getValue().contains(category)) {
                subscribers.add(entry.getKey());
            }
        }
        return Set.copyOf(subscribers);
    }
}
