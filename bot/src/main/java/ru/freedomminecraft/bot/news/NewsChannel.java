package ru.freedomminecraft.bot.news;
import java.util.ArrayList;
import java.util.List;

public class NewsChannel {
    private final long channelId;
    private final List<NewsListener> listeners = new ArrayList<>();

    public NewsChannel(long channelId){
        this.channelId = channelId;
    }

    public void subscribe(NewsListener listener){
        listeners.add(listener);
    }

    public void receive(NewsPost post){
        if (post.channelId() != channelId){
            return;
        }

        if (post.text() == null || post.text().isBlank()){
            return;
        }

        for (NewsListener listener : listeners){
            listener.onPost(post);
        }
    }
}
