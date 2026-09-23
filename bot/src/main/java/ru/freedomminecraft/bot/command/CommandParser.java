package ru.freedomminecraft.bot.command;

import java.util.Locale;
import java.util.Optional;


public class CommandParser {
    private static final String PREFIX = "/";

    private String removeBotMention(String word){
        int at = word.indexOf('@');
        return at >= 0 ? word.substring(0,at) : word;
    }

    public Optional<ParsedCommand> parse(String text) {
        if (text == null) {
            return Optional.empty();
        }

        String trimmed = text.strip();
        if (!trimmed.startsWith(PREFIX)) {
            return Optional.empty();
        }

        String withoutPrefix = trimmed.substring(PREFIX.length());
        String[] parts = withoutPrefix.split("\\s+", 2);

        String name = removeBotMention(parts[0]).toLowerCase(Locale.ROOT);
        if (name.isEmpty()) {
            return Optional.empty();
        }

        String argument = parts.length > 1 ? parts[1] : "";
        return Optional.of(new ParsedCommand(name, argument));
    }
}
