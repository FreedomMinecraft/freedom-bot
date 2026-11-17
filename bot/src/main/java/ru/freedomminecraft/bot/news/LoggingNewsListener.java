package ru.freedomminecraft.bot.news;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingNewsListener implements NewsListener {
    private static final Logger log = LoggerFactory.getLogger(LoggingNewsListener.class);

    @Override
    public void onPost(NewsPost post){
        log.info("Новый пост в канале {} (сообщение {}): {}", post.channelId(), post.messageId(), post.categories(), post.text());
    }
}
