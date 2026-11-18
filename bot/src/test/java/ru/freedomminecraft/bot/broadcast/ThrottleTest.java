package ru.freedomminecraft.bot.broadcast;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThrottleTest {
    @Test
    void waitsAtLeastGivenInterval(){
        Throttle throttle = Throttle.every(Duration.ofMillis(30));

        long start = System.nanoTime();
        throttle.pause();
        long waitedMillis = (System.nanoTime() - start) / 1_000_000;

        assertTrue(waitedMillis >= 30, "ждали всего " + waitedMillis + " мс");
    }
}
