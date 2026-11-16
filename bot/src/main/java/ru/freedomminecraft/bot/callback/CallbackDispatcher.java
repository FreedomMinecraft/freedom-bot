package ru.freedomminecraft.bot.callback;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallbackDispatcher {
    private static final Logger log = LoggerFactory.getLogger(CallbackDispatcher.class);

    static final String OUTDATED = "Кнопка устарела. Вызовите команду ещё раз.";
    static final String FAILED = "Не получилось. Попробуйте чуть позже.";

    private final Map<String, CallbackHandler> handlers = new HashMap<>();

    public void register(CallbackHandler handler) {
        String prefix = handler.prefix();
        if (prefix.isBlank() || prefix.contains(CallbackHandler.SEPARATOR)) {
            throw new IllegalArgumentException("Метка обработчика должна быть непустой и без \""
            + CallbackHandler.SEPARATOR + "\": " + prefix);
        }
        if (handlers.putIfAbsent(prefix, handler) != null) {
            throw new IllegalArgumentException("Обработчик с меткой " + prefix + " уже зарегистрирован");
        }
    }

    public CallbackResult dispatch(long chatId, long userId, int messageId, String data) {
        if (data == null) {
            return CallbackResult.notice(OUTDATED);
        }

        String[] parts = data.split(CallbackHandler.SEPARATOR, 2);
        String prefix = parts[0];
        String payload = parts.length > 1 ? parts[1] : "";

        CallbackHandler handler = handlers.get(prefix);
        if (handler == null) {
            log.info("Нажата кнопка без обработчика: {}", data);
            return CallbackResult.notice(OUTDATED);
        }

        log.info("Кнопка: {}", data);
        try {
            return handler.handle(new CallbackContext(chatId, userId, messageId, payload));
        } catch (RuntimeException e) {
            log.error("Ошибка при обработке кнопки {}", data, e);
            return CallbackResult.notice(FAILED);
        }
    }
}
