package ru.freedomminecraft.bot.subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import ru.freedomminecraft.bot.callback.CallbackContext;
import ru.freedomminecraft.bot.callback.CallbackHandler;
import ru.freedomminecraft.bot.callback.CallbackResult;
import ru.freedomminecraft.bot.format.Emoji;
import ru.freedomminecraft.bot.format.Html;
import ru.freedomminecraft.bot.news.NewsCategory;
import ru.freedomminecraft.bot.reply.InlineButton;
import ru.freedomminecraft.bot.reply.InlineKeyboard;
import ru.freedomminecraft.bot.reply.Reply;

public class SubscriptionMenu implements CallbackHandler {
    static final String ON = "✅ ";
    static final String OFF = "❌ ";
    static final String OUTDATED = "Кнопка устарела. Вызовите /subscribe еще раз.";

    private final SubscriptionRepository subscriptions;

    public SubscriptionMenu(SubscriptionRepository subscriptions){
        this.subscriptions = subscriptions;
    }

    @Override
    public String prefix(){
        return "sub";
    }

    public Reply menuFor(long userId){
        Set<NewsCategory> current = subscriptions.categoriesOf(userId);
        List<List<InlineButton>> rows = new ArrayList<>();
        for (NewsCategory category : NewsCategory.values()){
            String mark = current.contains(category) ? ON : OFF;
            InlineButton button = new InlineButton(mark + category.hashtag(), callbackData(category.name()));
            rows.add(List.of(button));
        }
        String text = Html.title(Emoji.BELL, "ПОДПИСКИ") + "\n\n"
            + "Выберите, какие новости канала присылать вам в личные сообщения.\n"
            + "Нажмите на категорию, чтобы включить или выключить её.";
        return Reply.withKeyboard(text, new InlineKeyboard(rows));
    }

    @Override
    public CallbackResult handle(CallbackContext context){
        NewsCategory category;
        try{
            category = NewsCategory.valueOf(context.payload());
        } catch (IllegalArgumentException e) {
            return CallbackResult.notice(OUTDATED);
        }

        long userId = context.userId();
        String notice;
        if (subscriptions.categoriesOf(userId).contains(category)){
            subscriptions.unsubscribe(userId, category);
            notice = "Вы отписались от " + category.hashtag();
        } else {
            subscriptions.subscribe(userId, category);
            notice = "Вы подписались на " + category.hashtag();
        }
        return new CallbackResult(Optional.of(notice), Optional.of(menuFor(userId)));
    }
}
