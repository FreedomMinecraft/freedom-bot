package ru.freedomminecraft.bot.format;

public enum Emoji {
    WAVE("5411394265924257943", "👋"),
    COMPASS("5404747503385983645", "🧭"),
    BELL("5458603043203327669", "🔔"),
    SHIELD("5413453947555822372", "🛡"),
    BOOK("5411369574157286161", "📖"),
    ROBOT("5355051922862653659", "🤖"),
    FLAG("5460755126761312667", "🚩"),
    GAMEPAD("5460754443861504837", "🎮"),
    GLOBE("5447410659077661506", "🌐"),
    STAR("5438496463044752972", "⭐"),
    CROWN("5217822164362739968", "👑"),
    SOS("5220108512893344933", "🆘"),
    PARTY("5461151367559141950", "🎉"),
    TV("5449831976955566100", "📺"),
    SPEECH("5443038326535759644", "💬"),
    THINKING("5316798753691147993", "🤔"),
    TOOLS("5215392879320505675", "🛠");

    private final String id;
    private final String fallback;

    Emoji(String id, String fallback) {
        this.id = id;
        this.fallback = fallback;
    }

    public String id() {
        return id;
    }

    public String html() {
        return "<tg-emoji emoji-id=\"" + id + "\">" + fallback + "</tg-emoji>";
    }
}
