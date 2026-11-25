package ru.freedomminecraft.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import ru.freedomminecraft.common.api.BotApi;

public final class FreedomPlugin extends JavaPlugin{
    @Override
    public void onEnable() {
        getLogger().info("Плагин включен, версия " + getPluginMeta().getVersion());
        getLogger().info("Адрес проверки связи с ботом: " + BotApi.PING_PATH);
    }

    @Override
    public void onDisable() {
        getLogger().info("Плагин выключен");
    }
}
