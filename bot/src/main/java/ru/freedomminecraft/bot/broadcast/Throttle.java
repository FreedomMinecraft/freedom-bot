package ru.freedomminecraft.bot.broadcast;

import java.time.Duration;

public interface Throttle {
    void pause();

    static Throttle every(Duration interval){
        return () -> {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        };
    }
}
