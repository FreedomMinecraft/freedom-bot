package ru.freedomminecraft.bot.serverinfo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FileServerInfoSource implements ServerInfoSource {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Path file;

    public FileServerInfoSource(Path file) {
        this.file = file;
    }

    @Override
    public ServerInfo load() {
        try (InputStream input = Files.newInputStream(file)) {
            return mapper.readValue(input, ServerInfo.class);
        } catch (NoSuchFileException e) {
            throw new IllegalStateException("Не найден файл с данными о сервере: " + file, e);
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать данные о сервере из " + file, e);
        }
    }

    @Override
    public String toString() {
        return "файл " + file;
    }
}
