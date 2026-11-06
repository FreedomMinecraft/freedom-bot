package ru.freedomminecraft.bot.command;

import ru.freedomminecraft.bot.format.Html;
import ru.freedomminecraft.bot.format.Emoji;

import java.util.Optional;

public class HelpCommand implements Command{
    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry){
        this.registry = registry;
    }

    @Override
    public String name(){
        return "help";
    }

    @Override
    public String description(){
        return "Список команд или справка по одной: help <команда>";
    }

    @Override
    public CommandGroup group() {
        return CommandGroup.BASIC;
    }

    @Override
    public String execute(String argument){
        if (argument == null || argument.isBlank()){
            StringBuilder text = new StringBuilder(Html.title(Emoji.BOOK, "КОМАНДЫ БОТА")).append("\n");
            for (CommandGroup group : CommandGroup.values()) {
                StringBuilder lines = new StringBuilder();
                for (Command command : registry.all()) {
                    if (command.group() == group) {
                        lines.append("/")
                            .append(command.name())
                            .append(" - ")
                            .append(Html.escape(command.description()))
                            .append("\n");
                    }
                }
                if (!lines.isEmpty()) {
                    text.append("\n").append(lines);
                }
            }
            return text.toString();
        }
        String commandName = CommandParser.normalizeName(argument);
        Optional<Command> found = registry.find(commandName);
        if (found.isPresent()){
            Command command = found.get();
            return "/" + command.name() + " — " + Html.escape(command.description());
        }
        return Emoji.THINKING.html() + " Нет команды «" + Html.escape(commandName) + "». Все команды — /help";
    }
}
