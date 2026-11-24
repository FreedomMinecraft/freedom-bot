package ru.freedomminecraft.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class FreedomPlugin extends JavaPlugin{
    @Override
    public void onEnable() {
        getLogger().info("Плагин включен, версия " + getPluginMeta().getVersion());
    }

    @Override
    public void onDisable() {
        getLogger().info("Плагин выключен");
    }
}
