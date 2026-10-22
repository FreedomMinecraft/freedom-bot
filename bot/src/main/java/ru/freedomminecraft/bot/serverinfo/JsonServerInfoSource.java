package ru.freedomminecraft.bot.serverinfo;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonServerInfoSource implements ServerInfoSource {
    public static final String DEFAULT_RESOURCE = "/server-info.json";

    private final ObjectMapper mapper = new ObjectMapper();
    private final String resource;

    public JsonServerInfoSource(String resource) {
        this.resource = resource;
    }

    @Override
    public ServerInfo load() {
        try (InputStream input = JsonServerInfoSource.class.getResourceAsStream(resource)) {
            if (input == null) {
                throw new IllegalStateException("Не найден файл с данными о сервере: " + resource);
            }
            return mapper.readValue(input, ServerInfo.class);
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать данные о сервере из " + resource, e);
        }
    }
}
