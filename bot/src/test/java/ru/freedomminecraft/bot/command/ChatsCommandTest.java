package ru.freedomminecraft.bot.command;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.freedomminecraft.bot.serverinfo.Chat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatsCommandTest {
    private final ChatsCommand chats = new ChatsCommand(List.of(
        new Chat("Общий чат", "Общение игроков", "https://t.me/example_chat"),
        new Chat("Торговля", "Покупка и продажа", "https://t.me/example_trade")));

    @Test
    void nameIsChats(){
        assertEquals("chats", chats.name());
    }

    @Test
    void showsNameAndDescription(){
        String answer = chats.execute("");

        assertTrue(answer.contains("Общий чат — Общение игроков"));
    }

    @Test
    void showsLinkOfEveryChat(){
        String answer = chats.execute("");

        assertTrue(answer.contains("https://t.me/example_chat"));
        assertTrue(answer.contains("https://t.me/example_trade"));
    }

    @Test
    void emptyListSaysSo(){
        ChatsCommand empty = new ChatsCommand(List.of());

        String answer = empty.execute("");

        assertTrue(answer.contains("пуст"));
    }

    @Test
    void appearsInHelpAutomatically(){
        CommandRegistry registry = new CommandRegistry();
        registry.register(new HelpCommand(registry));
        registry.register(chats);

        String helpAnswer = registry.find("help").get().execute("");

        assertTrue(helpAnswer.contains("/chats - Чаты сервера со ссылками"));
    }
}
