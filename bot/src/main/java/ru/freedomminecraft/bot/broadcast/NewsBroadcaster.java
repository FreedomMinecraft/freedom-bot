package ru.freedomminecraft.bot.broadcast;

import java.time.Duration;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.freedomminecraft.bot.news.NewsCategory;
import ru.freedomminecraft.bot.news.NewsListener;
import ru.freedomminecraft.bot.news.NewsPost;
import ru.freedomminecraft.bot.subscription.SubscriptionRepository;

public class NewsBroadcaster implements NewsListener {
    public static final Duration TELEGRAM_PAUSE = Duration.ofMillis(50);

    private static final Logger log = LoggerFactory.getLogger(NewsBroadcaster.class);

    private final SubscriptionRepository subscriptions;
    private final PostSender sender;
    private final Throttle throttle;
    private final Executor executor;

    public NewsBroadcaster(SubscriptionRepository subscriptions, PostSender sender, Throttle throttle, Executor executor){
        this.subscriptions = subscriptions;
        this.sender = sender;
        this.throttle = throttle;
        this.executor = executor;
    }

    @Override
    public void onPost(NewsPost post){
        executor.execute(() -> broadcast(post));
    }

    void broadcast(NewsPost post){
        try {
            Set<Long> recipients = recipientsOf(post);
            if (recipients.isEmpty()){
                log.info("Пост {}: нет подписчиков на категории {}", post.messageId(), post.categories());
                return;
            }
            int sent = 0;
            for (long userId : recipients) {
                throttle.pause();
                Delivery delivery = deliver(userId, post);
                if (delivery == Delivery.SENT){
                    sent++;
                } else if (delivery == Delivery.BLOCKED){
                    forget(userId);
                }
            }
            log.info("Пост {} разослан: доставлено {} из {}", post.messageId(), sent, recipients.size());
        } catch (RuntimeException e) {
            log.error("Рассылка поста {} прервана", post.messageId(), e);
        }
    }

    Set<Long> recipientsOf(NewsPost post){
        Set<Long> recipients = new TreeSet<>();
        for (NewsCategory category : post.categories()){
            recipients.addAll(subscriptions.subscribersOf(category));
        }
        return recipients;
    }

    private Delivery deliver(long userId, NewsPost post){
        try {
            return sender.send(userId, post);
        } catch (RuntimeException e) {
            log.error("Ошибка при отправке поста {} пользователю {}", post.messageId(), userId, e);
            return Delivery.FAILED;
        }
    }

    private void forget(long userId){
        for (NewsCategory category : subscriptions.categoriesOf(userId)){
            subscriptions.unsubscribe(userId, category);
        }
        log.info("Пользователь {} заблокировал бота: его подписки удалены", userId);
    }
}
