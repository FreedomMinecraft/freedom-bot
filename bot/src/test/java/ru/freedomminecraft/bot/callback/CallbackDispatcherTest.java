package ru.freedomminecraft.bot.callback;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.freedomminecraft.bot.reply.Reply;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CallbackDispatcherTest {
    private static final long CHAT_ID = 100L;
    private static final long USER_ID = 200L;
    private static final int MESSAGE_ID = 300;

    private CallbackDispatcher dispatcher;
    private RecordingHandler recording;

    private static class RecordingHandler implements CallbackHandler {
        private CallbackContext received;

        @Override
        public String prefix() {
            return "rec";
        }

        @Override
        public CallbackResult handle(CallbackContext context) {
            received = context;
            return CallbackResult.update(Reply.of("нажато: " + context.payload()));
        }
    }

    private static class BrokenHandler implements CallbackHandler {
        @Override
        public String prefix() {
            return "broken";
        }

        @Override
        public CallbackResult handle(CallbackContext context) {
            throw new IllegalStateException("обработчик сломался");
        }
    }

    private static CallbackHandler withPrefix(String prefix) {
        return new CallbackHandler() {
            @Override
            public String prefix() {
                return prefix;
            }

            @Override
            public CallbackResult handle(CallbackContext context) {
                return CallbackResult.silent();
            }
        };
    }

    @BeforeEach
    void setUp() {
        dispatcher = new CallbackDispatcher();
        recording = new RecordingHandler();
        dispatcher.register(recording);
        dispatcher.register(new BrokenHandler());
    }

    @Test
    void routesByPrefixAndReturnsHandlerResult() {
        CallbackResult result = dispatcher.dispatch(CHAT_ID, USER_ID, MESSAGE_ID, "rec:EVENT");

        assertEquals(CallbackResult.update(Reply.of("нажато: EVENT")), result);
    }

    @Test
    void passesChatUserAndMessageToHandler() {
        dispatcher.dispatch(CHAT_ID, USER_ID, MESSAGE_ID, "rec:EVENT");

        assertEquals(new CallbackContext(CHAT_ID, USER_ID, MESSAGE_ID, "EVENT"), recording.received);
    }

    @Test
    void payloadKeepsSeparators() {
        dispatcher.dispatch(CHAT_ID, USER_ID, MESSAGE_ID, "rec:a:b");

        assertEquals("a:b", recording.received.payload());
    }

    @Test
    void missingPayloadIsEmpty() {
        dispatcher.dispatch(CHAT_ID, USER_ID, MESSAGE_ID, "rec");

        assertEquals("", recording.received.payload());
    }

    @Test
    void unknownPrefixSaysButtonIsOutdated() {
        CallbackResult result = dispatcher.dispatch(CHAT_ID, USER_ID, MESSAGE_ID, "old:EVENT");

        assertEquals(CallbackResult.notice(CallbackDispatcher.OUTDATED), result);
    }

    @Test
    void missingDataSaysButtonIsOutdated() {
        CallbackResult result = dispatcher.dispatch(CHAT_ID, USER_ID, MESSAGE_ID, null);

        assertEquals(CallbackResult.notice(CallbackDispatcher.OUTDATED), result);
    }

    @Test
    void handlerErrorDoesNotEscape() {
        CallbackResult result = dispatcher.dispatch(CHAT_ID, USER_ID, MESSAGE_ID, "broken:x");

        assertEquals(CallbackResult.notice(CallbackDispatcher.FAILED), result);
    }

    @Test
    void rejectsDuplicatePrefix() {
        assertThrows(IllegalArgumentException.class, () -> dispatcher.register(withPrefix("rec")));
    }

    @Test
    void rejectsPrefixWithSeparator() {
        assertThrows(IllegalArgumentException.class, () -> dispatcher.register(withPrefix("a:b")));
    }

    @Test
    void handlerBuildsDataForItsButtons() {
        assertEquals("rec:EVENT", recording.callbackData("EVENT"));
    }

    @Test
    void dataBuiltByHandlerComesBackToIt() {
        dispatcher.dispatch(CHAT_ID, USER_ID, MESSAGE_ID, recording.callbackData("GIVEAWAY"));

        assertEquals("GIVEAWAY", recording.received.payload());
    }
}
