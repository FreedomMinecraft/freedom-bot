package ru.freedomminecraft.bot.broadcast;

public enum Delivery {
    /** Пост доставлен. */
    SENT,
    /** Пользователь заблокировал бота: писать ему больше нельзя. */
    BLOCKED,
    /** Не получилось сейчас (сеть, лимиты, пользователь не открывал бота). */
    FAILED
}
