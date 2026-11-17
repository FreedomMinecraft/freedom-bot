package ru.freedomminecraft.bot.news;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NewsChannelTest {
    private static final long OUR_CHANNEL = -1337228;

    private NewsChannel channel;
    private RecordingListener listener;

    @BeforeEach
    void setUp(){
        channel = new NewsChannel(OUR_CHANNEL);
        listener = new RecordingListener();
        channel.subscribe(listener);
    }

    @Test
    void passesPostFromOurChannelToListener(){
        NewsPost post = new NewsPost(OUR_CHANNEL, 7, "завтра ивент! #ивент");

        channel.receive(post);
        assertEquals(List.of(post), listener.received);
    }

    @Test
    void ignoresPostFromOtherChannel(){
        channel.receive(new NewsPost(-2281337, 7, "Чужой пост"));

        assertTrue(listener.received.isEmpty());
    }

    @Test
    void ignoresPostWithoutText(){
        channel.receive(new NewsPost(OUR_CHANNEL, 7, null));
        channel.receive(new NewsPost(OUR_CHANNEL, 8, "   "));

        assertTrue(listener.received.isEmpty());
    }

    @Test
    void notifiesEveryListener(){
        RecordingListener second = new RecordingListener();
        channel.subscribe(second);
        NewsPost post = new NewsPost(OUR_CHANNEL, 7, "розыгрыш! #розыгрыш");

        channel.receive(post);

        assertEquals(List.of(post), listener.received);
        assertEquals(List.of(post), second.received);
    }

    private static class RecordingListener implements NewsListener {
        private final List<NewsPost> received = new ArrayList<>();

        @Override
        public void onPost(NewsPost post){
            received.add(post);
        }
    }
}
