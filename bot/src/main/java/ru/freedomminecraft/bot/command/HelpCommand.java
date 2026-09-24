package ru.freedomminecraft.bot.command;

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
    public String execute(String argument){
        if (argument == null || argument.isBlank()){
            StringBuilder text = new StringBuilder("Доступные команды:\n");
            for (Command command : registry.all()){
                text.append("/")
                    .append(command.name())
                    .append(" - ")
                    .append(command.description())
                    .append("\n");
            }
            return text.toString();
        }
        String commandName = CommandParser.normalizeName(argument);
        Optional<Command> found = registry.find(commandName);
        if (found.isPresent()){
            Command command = found.get();
            return "/" + command.name() + " - " + command.description();
        }
        return "Нет такой команды: " + commandName + ". Напиши /help, чтобы увидеть список команд.";
    }
}
