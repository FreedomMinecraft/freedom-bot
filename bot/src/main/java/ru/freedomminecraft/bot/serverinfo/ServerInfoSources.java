package ru.freedomminecraft.bot.serverinfo;

import java.nio.file.Path;
import java.util.Map;

public final class ServerInfoSources {
    public static final String FILE_VARIABLE = "SERVER_INFO_FILE";

    private ServerInfoSources(){}

    public static ServerInfoSource fromEnvironment(Map<String, String> environment) {
        String path = environment.get(FILE_VARIABLE);
        if (path == null || path.isBlank()) {
            return new JsonServerInfoSource(JsonServerInfoSource.DEFAULT_RESOURCE);
        }
        return new FileServerInfoSource(Path.of(path.strip()));
    }
}
