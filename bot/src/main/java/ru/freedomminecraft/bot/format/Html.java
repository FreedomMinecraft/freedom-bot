package ru.freedomminecraft.bot.format;

public final class Html {
    private Html() {}

        public static String escape(String text){
            return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
        }
}
