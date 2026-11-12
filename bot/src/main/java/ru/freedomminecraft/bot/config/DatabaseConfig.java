package ru.freedomminecraft.bot.config;
import java.util.Map;
import java.util.Optional;

public record DatabaseConfig(String url, String user, String password) {
    public static final String URL_VARIABLE = "DB_URL";
    public static final String USER_VARIABLE = "DB_USER";
    public static final String PASSWORD_VARIABLE = "DB_PASSWORD";

    public static Optional<DatabaseConfig> fromEnvironment(Map<String, String> environment){
        Optional<String> url = readOptional(environment, URL_VARIABLE);
        if (url.isEmpty()){
            return Optional.empty();
        }
        String user = readOptional(environment, USER_VARIABLE).orElse(null);
        String password = readOptional(environment, PASSWORD_VARIABLE).orElse(null);
        return Optional.of(new DatabaseConfig(url.get(), user, password));
    }

    private static Optional<String> readOptional(Map<String, String> environment, String name){
        String value = environment.get(name);
        if (value == null || value.isBlank()){
            return Optional.empty();
        }
        return Optional.of(value.strip());
    }

    @Override
    public String toString(){
        return "DatabaseConfig[url=" + url + ", user=" + user + ", password=****]";
    }
}
