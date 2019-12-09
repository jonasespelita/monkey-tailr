package com.ti.a0284021.monkeytailr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@Slf4j
public class TailrWsUnsubscribeListener implements
        ApplicationListener<SessionUnsubscribeEvent> {

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent event) {
        log.debug("UNSUBBING!");
        log.debug("Message {}", new String(event.getMessage().getPayload()));
        log.debug("Headers {}", event.getMessage().getHeaders());
        log.debug("Source {}", event.getSource());

    }
}
