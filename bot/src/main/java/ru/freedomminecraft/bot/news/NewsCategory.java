package ru.freedomminecraft.bot.news;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum NewsCategory {
    IMPORTANT("#важное"),
    EVENT("#ивент"),
    GIVEAWAY("#розыгрыш");

    private static final Pattern HASHTAG = Pattern.compile("#[\\p{L}\\p{N}_]+");
    private final String hashtag;

    NewsCategory(String hashtag){
        this.hashtag = hashtag;
    }

    public String hashtag(){
        return hashtag;
    }

    public static Set<NewsCategory> fromText(String text){
        Set<NewsCategory> categories = EnumSet.noneOf(NewsCategory.class);
        if (text == null){
            return Collections.unmodifiableSet(categories);
        }
        Matcher matcher = HASHTAG.matcher(text.toLowerCase(Locale.ROOT));
        while (matcher.find()){
            String found = matcher.group();
            for (NewsCategory category : values()){
                if (category.hashtag.equals(found)){
                    categories.add(category);
                }
            }
        }
        return Collections.unmodifiableSet(categories);
    }
}
