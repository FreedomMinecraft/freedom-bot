package ru.freedomminecraft.bot.format;

import jdk.nio.mapmode.ExtendedMapMode;

public final class Html {
    public static final String BULLET = "▫️ ";
    private Html() {}

    public static String escape(String text){
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }
    /**Жирный текст*/
    public static String bold (String text) {
        return "<b>" + escape(text) + "</b>";
    }
    /**Моноширинный*/
    public static String code(String text) {
        return "<code>" + escape(text) + "</code>";
    }
    /**Ссылка в тексте*/
    public static String link (String text, String url) {
        return "<a href=\"" + escape(url).replace("\"", "&quot;") + "\">" + escape(text) + "</a>";
    }

    public static String title(Emoji emoji, String text) {
        return emoji.html() + " " + bold(text) + " " + emoji.html();
    }
}
