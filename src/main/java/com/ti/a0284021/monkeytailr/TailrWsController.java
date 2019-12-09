package com.ti.a0284021.monkeytailr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TailrWsController {
    private final TailrService tailrService;

    @PostConstruct
    private void init() {
        log.info("{} initialized!", this.getClass().getSimpleName());
    }

    @MessageMapping("/test")
    public String test() {

        log.info("I'm in test!");
        return "Hello there!";
    }
}
