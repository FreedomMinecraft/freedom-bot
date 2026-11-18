package ru.freedomminecraft.bot.command.news;

import ru.freedomminecraft.bot.command.Command;
import ru.freedomminecraft.bot.command.CommandContext;
import ru.freedomminecraft.bot.command.CommandGroup;
import ru.freedomminecraft.bot.reply.Reply;
import ru.freedomminecraft.bot.subscription.SubscriptionMenu;

public class SubscribeCommand implements Command {
    private final SubscriptionMenu menu;

    public SubscribeCommand(SubscriptionMenu menu){
        this.menu = menu;
    }

    @Override
    public String name(){
        return "subscribe";
    }

    @Override
    public String description(){
        return "Выбрать, какие новости присылать";
    }

    @Override
    public CommandGroup group(){
        return CommandGroup.BASIC;
    }

    @Override
    public Reply execute(CommandContext context){
        return menu.menuFor(context.userId());
    }
}
