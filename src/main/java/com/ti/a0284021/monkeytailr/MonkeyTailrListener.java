package com.ti.a0284021.monkeytailr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * The {@code MonkeyTailrListener} class is a listener adapter for {@link org.apache.commons.io.input.TailerListener}
 * that sends tail values to web socket destination defined in {@code /topic/logs}.
 *
 * @author a0284021
 */
@Slf4j
@RequiredArgsConstructor
public class MonkeyTailrListener extends TailerListenerAdapter {
    /**
     * Web socket topic for tail log values.
     */
    private static final String DESTINATION = "/topic/logs/";

    /**
     * Web Socket Messaging template.
     */
    private final SimpMessagingTemplate msgTemplate;

    /**
     * File key to differentiate topic for current listener. Needed to be able to support multiple listeners.
     */
    private final String fileKey;

    @Override
    public void handle(String line) {
        log.trace(line);
        msgTemplate.convertAndSend(DESTINATION + fileKey, line);
    }
}
