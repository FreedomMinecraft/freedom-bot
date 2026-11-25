package ru.freedomminecraft.common.api;



public final class BotApi {
    /** Заголовок запроса с секретным ключом API */
    public static final String KEY_HEADER = "X-Api-Key";
    /** Адрес проверки связи: бот отвечает, что он на месте */
    public static final String PING_PATH = "/api/ping";

    private BotApi(){}
}
