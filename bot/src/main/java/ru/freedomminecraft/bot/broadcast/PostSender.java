package ru.freedomminecraft.bot.broadcast;

import ru.freedomminecraft.bot.news.NewsPost;

public interface PostSender {
    Delivery send(long userId, NewsPost post);
}
