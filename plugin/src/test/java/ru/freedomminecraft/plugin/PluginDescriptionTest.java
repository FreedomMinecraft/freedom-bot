package ru.freedomminecraft.plugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;

import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PluginDescriptionTest {

    private PluginDescriptionFile description;

    @BeforeEach
    void readDescription() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("/plugin.yml")) {
            assertNotNull(in, "plugin.yml не найден в ресурсах");
            description = new PluginDescriptionFile(in);
        }
    }

    @Test
    void mainPointsToPluginClass() {
        assertEquals(FreedomPlugin.class.getName(), description.getMain());
    }

    @Test
    void versionIsSubstitutedByMaven() {
        assertFalse(description.getVersion().contains("${"));
    }
}
