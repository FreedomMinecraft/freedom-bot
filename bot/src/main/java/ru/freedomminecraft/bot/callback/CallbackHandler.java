package ru.freedomminecraft.bot.callback;

public interface CallbackHandler {
    String SEPARATOR = ":";

    String prefix();

    CallbackResult handle(CallbackContext context);

    default String callbackData(String payload) {
        return prefix() + SEPARATOR + payload;
    }
}
