package ru.freedomminecraft.bot.reply;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InlineKeyboardTest {
    private static final InlineButton YES = new InlineButton("Да", "yes");
    private static final InlineButton NO = new InlineButton("Нет", "no");

    @Test
    void keepsRowsInOrder() {
        InlineKeyboard keyboard = new InlineKeyboard(List.of(List.of(YES, NO), List.of(NO)));

        assertEquals(List.of(YES, NO), keyboard.rows().getFirst());
        assertEquals(List.of(NO), keyboard.rows().get(1));
    }

    @Test
    void rejectsKeyboardWithoutRows() {
        assertThrows(IllegalArgumentException.class, () -> new InlineKeyboard(List.of()));
    }

    @Test
    void rejectsEmptyRow() {
        assertThrows(IllegalArgumentException.class,
            () -> new InlineKeyboard(List.of(List.of(YES), List.of())));
    }

    @Test
    void changingOriginalListDoesNotChangeKeyboard() {
        List<InlineButton> row = new ArrayList<>(List.of(YES));
        InlineKeyboard keyboard = new InlineKeyboard(List.of(row));

        row.add(NO);
        assertEquals(List.of(YES), keyboard.rows().getFirst());
    }

    @Test
    void rowsCannotBeChangedFromOutside() {
        InlineKeyboard keyboard = new InlineKeyboard(List.of(List.of(YES)));

        assertThrows(UnsupportedOperationException.class, () -> keyboard.rows().getFirst().add(NO));
    }
}
