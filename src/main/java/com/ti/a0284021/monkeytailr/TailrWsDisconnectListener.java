package com.ti.a0284021.monkeytailr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class TailrWsDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        log.debug("DISCONNECTED!");
        log.debug("Message {}", new String(event.getMessage().getPayload()));
        log.debug("Headers {}", event.getMessage().getHeaders());
        log.debug("Source {}", event.getSource());

        // do cleanup
    }
}
