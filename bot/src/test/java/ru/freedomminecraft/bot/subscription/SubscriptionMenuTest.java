package ru.freedomminecraft.bot.subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.freedomminecraft.bot.callback.CallbackContext;
import ru.freedomminecraft.bot.callback.CallbackResult;
import ru.freedomminecraft.bot.news.NewsCategory;
import ru.freedomminecraft.bot.reply.InlineButton;
import ru.freedomminecraft.bot.reply.Reply;

import javax.security.auth.callback.Callback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubscriptionMenuTest {
    private static final long USER_ID = 200L;
    private static final long OTHER_USER_ID = 201L;

    private SubscriptionRepository subscriptions;
    private SubscriptionMenu menu;

    @BeforeEach
    void setUp(){
        subscriptions = new InMemorySubscriptionRepository();
        menu = new SubscriptionMenu(subscriptions);
    }

    private static List<InlineButton> buttons(Reply reply){
        List<InlineButton> buttons = new ArrayList<>();
        for (List<InlineButton> row : reply.keyboard().orElseThrow().rows()){
            buttons.add(row.get(0));
        }
        return buttons;
    }

    private CallbackResult press(long userId, String payload){
        return menu.handle(new CallbackContext(100L, userId, 300, payload));
    }

    @Test
    void menuHasButtonForEveryCategory(){
        List<InlineButton> buttons = buttons(menu.menuFor(USER_ID));

        assertEquals(NewsCategory.values().length, buttons.size());
        assertEquals("sub:IMPORTANT", buttons.get(0).callbackData());
        assertEquals("sub:EVENT", buttons.get(1).callbackData());
        assertEquals("sub:GIVEAWAY", buttons.get(2).callbackData());
    }

    @Test
    void newUserSeesEverythingOff(){
        for (InlineButton button : buttons(menu.menuFor(USER_ID))){
            assertTrue(button.text().startsWith(SubscriptionMenu.OFF));
        }
    }

    @Test
    void menuShowsCurrentSubscriptions(){
        subscriptions.subscribe(USER_ID, NewsCategory.EVENT);

        List<InlineButton> buttons = buttons(menu.menuFor(USER_ID));

        assertEquals(SubscriptionMenu.OFF + "#важное", buttons.get(0).text());
        assertEquals(SubscriptionMenu.ON + "#ивент", buttons.get(1).text());
        assertEquals(SubscriptionMenu.OFF + "#розыгрыш", buttons.get(2).text());
    }

    @Test
    void pressingButtonSubscribes(){
        press(USER_ID, "EVENT");

        assertEquals(Set.of(NewsCategory.EVENT), subscriptions.categoriesOf(USER_ID));
    }

    @Test
    void pressingAgainUnsubscribes(){
        press(USER_ID, "EVENT");
        press(USER_ID, "EVENT");

        assertTrue(subscriptions.categoriesOf(USER_ID).isEmpty());
    }

    @Test
    void pressRedrawsMenuWithNewState(){
        CallbackResult result = press(USER_ID, "GIVEAWAY");

        List<InlineButton> buttons = buttons(result.update().orElseThrow());
        assertEquals(SubscriptionMenu.ON + "#розыгрыш", buttons.get(2).text());
    }

    @Test
    void pressShowsWhatChanged(){
        assertEquals("Вы подписались на #ивент", press(USER_ID, "EVENT").notice().orElseThrow());
        assertEquals("Вы отписались от #ивент", press(USER_ID, "EVENT").notice().orElseThrow());
    }

    @Test
    void pressChangesOnlyThatUser(){
        press(USER_ID, "IMPORTANT");

        assertTrue(subscriptions.categoriesOf(OTHER_USER_ID).isEmpty());
    }

    @Test
    void unknownCategoryMeansOutdatedButton(){
        CallbackResult result = press(USER_ID, "WEATHER");

        assertEquals(CallbackResult.notice(SubscriptionMenu.OUTDATED), result);
        assertTrue(subscriptions.categoriesOf(USER_ID).isEmpty());
    }
}
