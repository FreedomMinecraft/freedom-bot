package ru.freedomminecraft.bot.reply;

import java.util.ArrayList;
import java.util.List;

public record InlineKeyboard(List<List<InlineButton>> rows) {

    public InlineKeyboard {
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("В клавиатуре нет ни одного ряда");
        }
        List<List<InlineButton>> copy = new ArrayList<>();
        for (List<InlineButton> row : rows) {
            if (row.isEmpty()) {
                throw new IllegalArgumentException("В клавиатуре пустой ряд");
            }
            copy.add(List.copyOf(row));
        }
        rows = List.copyOf(copy);
    }
}
