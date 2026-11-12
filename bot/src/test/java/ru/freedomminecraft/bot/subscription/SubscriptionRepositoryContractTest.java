package ru.freedomminecraft.bot.subscription;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.freedomminecraft.bot.news.NewsCategory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class SubscriptionRepositoryContractTest {
    private static final long ALICE = 1L;
    private static final long BOB = 2L;

    private SubscriptionRepository repository;

    protected abstract SubscriptionRepository createRepository() throws Exception;

    @BeforeEach
    void setUp() throws Exception {
        repository = createRepository();
    }

    @Test
    void newUserHasNoSubscriptions() {
        assertTrue(repository.categoriesOf(ALICE).isEmpty());
    }

    @Test
    void subscribeAddsCategory() {
        repository.subscribe(ALICE, NewsCategory.EVENT);

        assertEquals(Set.of(NewsCategory.EVENT), repository.categoriesOf(ALICE));
    }

    @Test
    void subscribingTwiceKeepsOneSubscription() {
        repository.subscribe(ALICE, NewsCategory.EVENT);
        repository.subscribe(ALICE, NewsCategory.EVENT);

        assertEquals(Set.of(NewsCategory.EVENT), repository.categoriesOf(ALICE));
    }

    @Test
    void unsubscribeRemovesOnlyThatCategory() {
        repository.subscribe(ALICE, NewsCategory.EVENT);
        repository.subscribe(ALICE, NewsCategory.GIVEAWAY);

        repository.unsubscribe(ALICE, NewsCategory.EVENT);

        assertEquals(Set.of(NewsCategory.GIVEAWAY), repository.categoriesOf(ALICE));
    }

    @Test
    void unsubscribeWithoutSubscriptionDoesNothing() {
        repository.unsubscribe(ALICE, NewsCategory.EVENT);

        assertTrue(repository.categoriesOf(ALICE).isEmpty());
    }

    @Test
    void usersDoNotAffectEachOther() {
        repository.subscribe(ALICE, NewsCategory.EVENT);

        assertTrue(repository.categoriesOf(BOB).isEmpty());
    }

    @Test
    void subscribersOfReturnsOnlySubscribedUsers() {
        repository.subscribe(ALICE, NewsCategory.EVENT);
        repository.subscribe(BOB, NewsCategory.IMPORTANT);

        assertEquals(Set.of(ALICE), repository.subscribersOf(NewsCategory.EVENT));
        assertTrue(repository.subscribersOf(NewsCategory.GIVEAWAY).isEmpty());
    }

    @Test
    void unsubscribedUserIsNotInSubscribers() {
        repository.subscribe(ALICE, NewsCategory.EVENT);
        repository.unsubscribe(ALICE, NewsCategory.EVENT);

        assertTrue(repository.subscribersOf(NewsCategory.EVENT).isEmpty());
    }

    @Test
    void returnedSetCannotChangeRepository() {
        repository.subscribe(ALICE, NewsCategory.EVENT);
        Set<NewsCategory> categories = repository.categoriesOf(ALICE);

        assertThrows(UnsupportedOperationException.class, () -> categories.add(NewsCategory.GIVEAWAY));
    }
}
