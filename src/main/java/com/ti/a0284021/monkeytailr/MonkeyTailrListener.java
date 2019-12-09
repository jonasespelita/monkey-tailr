package com.ti.a0284021.monkeytailr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Slf4j
@RequiredArgsConstructor
public class MonkeyTailrListener extends TailerListenerAdapter {
    private final SimpMessagingTemplate msgTemplate;
    private final String fileKey;

    @Override
    public void handle(String line) {
        log.debug(line);
        msgTemplate.convertAndSend("/topic/logs/" + fileKey, line);
    }
}
