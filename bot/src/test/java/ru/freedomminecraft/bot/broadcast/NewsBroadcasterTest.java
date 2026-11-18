package ru.freedomminecraft.bot.broadcast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.freedomminecraft.bot.news.NewsCategory;
import ru.freedomminecraft.bot.news.NewsPost;
import ru.freedomminecraft.bot.subscription.InMemorySubscriptionRepository;
import ru.freedomminecraft.bot.subscription.SubscriptionRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NewsBroadcasterTest {
    private static final long CHANNEL = -1337228L;
    private static final long ALICE = 1L;
    private static final long BOB = 2L;
    private static final long CAROL = 3L;

    private SubscriptionRepository subscriptions;
    private RecordingSender sender;
    private int pauses;
    private NewsBroadcaster broadcaster;

    @BeforeEach
    void setUp(){
        subscriptions = new InMemorySubscriptionRepository();
        sender = new RecordingSender();
        pauses = 0;
        broadcaster = new NewsBroadcaster(subscriptions, sender, () -> pauses++, Runnable::run);
    }

    private static NewsPost post(String text) {
        return new NewsPost(CHANNEL, 7, text);
    }

    @Test
    void sendsPostToSubscribersOfItsCategory(){
        subscriptions.subscribe(ALICE, NewsCategory.EVENT);
        subscriptions.subscribe(BOB, NewsCategory.EVENT);
        NewsPost post = post("Турнир в субботу #ивент");

        broadcaster.onPost(post);

        assertEquals(List.of(ALICE, BOB), sender.recipients);
        assertEquals(List.of(post, post), sender.posts);
    }

    @Test
    void skipsSubscribersOfOtherCategories(){
        subscriptions.subscribe(ALICE, NewsCategory.EVENT);
        subscriptions.subscribe(BOB, NewsCategory.GIVEAWAY);

        broadcaster.onPost(post("Турнир #ивент"));

        assertEquals(List.of(ALICE), sender.recipients);
    }

    @Test
    void sendsToSubscribersOfEveryCategoryOfPost(){
        subscriptions.subscribe(ALICE, NewsCategory.EVENT);
        subscriptions.subscribe(BOB, NewsCategory.GIVEAWAY);

        broadcaster.onPost(post("Розыгрыш на ивенте #ивент #розыгрыш"));

        assertEquals(List.of(ALICE, BOB), sender.recipients);
    }

    @Test
    void subscribersOfTwoCategoriesGetsPostOnce(){
        subscriptions.subscribe(ALICE, NewsCategory.EVENT);
        subscriptions.subscribe(ALICE, NewsCategory.GIVEAWAY);

        broadcaster.onPost(post("Розыгрыш на ивенте #ивент #розыгрыш"));

        assertEquals(List.of(ALICE), sender.recipients);
    }

    @Test
    void postWithoutCategoryIsNotSent(){
        subscriptions.subscribe(ALICE, NewsCategory.EVENT);

        broadcaster.onPost(post("Просто пост без хештегов"));

        assertTrue(sender.recipients.isEmpty());
    }

    @Test
    void pausesBeforeEverySend(){
        subscriptions.subscribe(ALICE, NewsCategory.IMPORTANT);
        subscriptions.subscribe(BOB, NewsCategory.IMPORTANT);
        subscriptions.subscribe(CAROL, NewsCategory.IMPORTANT);

        broadcaster.onPost(post("Техработы #важное"));

        assertEquals(3, pauses);
    }

    @Test
    void userWhoBlockedBotLosesSubscriptions(){
        subscriptions.subscribe(ALICE, NewsCategory.EVENT);
        subscriptions.subscribe(ALICE, NewsCategory.IMPORTANT);
        sender.results.put(ALICE, Delivery.BLOCKED);

        broadcaster.onPost(post("Турнир #ивент"));

        assertTrue(subscriptions.categoriesOf(ALICE).isEmpty());
    }

    @Test
    void failedDeliveryKeepsSubscriptions(){
        subscriptions.subscribe(ALICE, NewsCategory.EVENT);
        sender.results.put(ALICE, Delivery.FAILED);

        broadcaster.onPost(post("Турнир #ивент"));

        assertEquals(Set.of(NewsCategory.EVENT), subscriptions.categoriesOf(ALICE));
    }

    @Test
    void brokenSendToOneUserDoesNotStopOthers(){
        subscriptions.subscribe(ALICE, NewsCategory.EVENT);
        subscriptions.subscribe(BOB, NewsCategory.EVENT);
        sender.brokenFor = ALICE;

        broadcaster.onPost(post("Турнир #ивент"));

        assertEquals(List.of(BOB), sender.recipients);
    }

    @Test
    void broadcastRunsOnGivenExecutor(){
        subscriptions.subscribe(ALICE, NewsCategory.EVENT);
        List<Runnable> queued = new ArrayList<>();
        NewsBroadcaster background = new NewsBroadcaster(subscriptions, sender, () -> {}, queued::add);

        background.onPost(post("Турнир #ивент"));

        assertTrue(sender.recipients.isEmpty());
        queued.get(0).run();
        assertEquals(List.of(ALICE), sender.recipients);
    }

    private static class RecordingSender implements PostSender {
        private final List<Long> recipients = new ArrayList<>();
        private final List<NewsPost> posts = new ArrayList<>();
        private final Map<Long, Delivery> results = new HashMap<>();
        private Long brokenFor;

        @Override
        public Delivery send(long userId, NewsPost post) {
            if (brokenFor != null && brokenFor == userId) {
                throw new IllegalStateException("отправка сломалась");
            }
            recipients.add(userId);
            posts.add(post);
            return results.getOrDefault(userId, Delivery.SENT);
        }
    }
}
