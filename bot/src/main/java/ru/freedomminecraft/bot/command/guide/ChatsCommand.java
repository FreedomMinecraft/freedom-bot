package ru.freedomminecraft.bot.command.guide;

import java.util.List;

import ru.freedomminecraft.bot.command.Command;
import ru.freedomminecraft.bot.command.CommandContext;
import ru.freedomminecraft.bot.command.CommandGroup;
import ru.freedomminecraft.bot.format.Emoji;
import ru.freedomminecraft.bot.format.Html;
import ru.freedomminecraft.bot.serverinfo.Chat;

public class ChatsCommand implements Command {
    private final List<Chat> chats;

    public ChatsCommand(List<Chat> chats){
        this.chats = List.copyOf(chats);
    }

    @Override
    public String name(){
        return "chats";
    }

    @Override
    public String description(){
        return "Чаты и каналы сервера";
    }

    @Override
    public CommandGroup group() {
        return CommandGroup.GUIDE;
    }

    @Override
    public String execute(CommandContext context){
        StringBuilder text = new StringBuilder(Html.title(Emoji.SPEECH, "ЧАТЫ И КАНАЛЫ FREEDOM")).append("\n\n");
        if (chats.isEmpty()){
            return text.append("Список пока пуст.").toString();
        }
        for (Chat chat : chats){
            text.append(Html.BULLET)
                .append(Html.link(chat.name(), chat.url()))
                .append(" — ")
                .append(Html.escape(chat.description()))
                .append("\n");
        }
        return text.toString().strip();
    }
}
