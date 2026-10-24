package ru.freedomminecraft.bot.command;

import java.util.List;

import ru.freedomminecraft.bot.format.Html;
import ru.freedomminecraft.bot.serverinfo.Chat;

public class ChatsCommand implements Command{
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
        return "Чаты сервера со ссылками";
    }

    @Override
    public String execute(String argument){
        if (chats.isEmpty()){
            return "Чаты сервера:\nСписок пока пуст.";
        }
        StringBuilder text = new StringBuilder("Чаты сервера:\n");
        for (Chat chat : chats){
            text.append("• ")
                .append(Html.escape(chat.name()))
                .append(" — ")
                .append(Html.escape(chat.description()))
                .append("\n")
                .append(Html.escape(chat.url()))
                .append("\n\n");
        }
        return text.toString();
    }
}
