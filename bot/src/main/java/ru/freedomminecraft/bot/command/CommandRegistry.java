package ru.freedomminecraft.bot.command;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommandRegistry {
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public void register(Command command) {
        String name = command.name();
        if (commands.containsKey(name)) {
            throw new IllegalArgumentException("Команда уже зарегистрирована: " + name);
        }
        commands.put(name, command);
    }

    public Optional<Command> find(String name) {
        return Optional.ofNullable(commands.get(name));
    }

    public List<Command> all() {
        return List.copyOf(commands.values());
    }
}
