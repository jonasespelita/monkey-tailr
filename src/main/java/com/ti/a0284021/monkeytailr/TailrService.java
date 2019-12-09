package com.ti.a0284021.monkeytailr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.Tailer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class TailrService implements DisposableBean {

    private final TailrFileProperties fileConfig;
    private final SimpMessagingTemplate simpMessagingTemplate;


    private final Map<String, Tailer> fileTailerMap = new ConcurrentHashMap<>();


    @PostConstruct
    private void init() {
        // start tailing files and publishing appropriately
        fileConfig.getFiles().forEach(
                (fileKey, location) -> {
                    fileTailerMap.put(
                            fileKey, Tailer.create(
                                    new File(location),
                                    StandardCharsets.UTF_8,
                                    new MonkeyTailrListener(simpMessagingTemplate, fileKey),
                                    1000,
                                    true, false, 4096));
                });

    }


    @Override
    public void destroy() {
        // closes all tailed files.
        fileTailerMap.values()
                .forEach(Tailer::stop);
    }
}
