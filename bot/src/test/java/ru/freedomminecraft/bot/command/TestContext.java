package ru.freedomminecraft.bot.command;

public final class TestContext {
    public static final long CHAT_ID = 100L;
    public static final long USER_ID = 200L;

    private TestContext() {}

    public static CommandContext of(String argument) {
        return new CommandContext(CHAT_ID, USER_ID, argument);
    }
}
