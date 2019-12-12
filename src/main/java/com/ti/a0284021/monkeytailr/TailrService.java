package com.ti.a0284021.monkeytailr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.io.input.Tailer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

@SuppressWarnings("WeakerAccess")
@Service
@RequiredArgsConstructor
@Slf4j
public class TailrService implements DisposableBean {

    private final TailrFileProperties fileConfig;
    private final SimpMessagingTemplate simpMessagingTemplate;


    private final Map<String, Tailer> fileTailerMap = new ConcurrentHashMap<>();

    /**
     * Service initializer that starts tailing configured files.
     */
    @PostConstruct
    private void init() {
        // start tailing files and publishing appropriately
        fileConfig.getFiles()
                .forEach((fileKey, location) -> {
                    final Tailer myTailer = Tailer.create(
                            new File(location),
                            UTF_8,
                            new MonkeyTailrListener(simpMessagingTemplate, fileKey),
                            1000,
                            true, false, 4096);

                    fileTailerMap.put(
                            fileKey,
                            myTailer);
                });

    }


    /**
     * Stops tailing all files on bean destroy.
     */
    @Override
    public void destroy() {
        // closes all tailed files.
        fileTailerMap.values()
                .forEach(Tailer::stop);
    }

    /**
     * Gets the tail of the file defined in fileKey.
     *
     * @param fileKey    key of file to get tail
     * @param numOfLines number of lines to retrieve
     * @return string value of log tail result
     * @throws IOException failed to read file
     */
    public String getTail(String fileKey, long numOfLines) throws IOException {
        final String fileLocation = fileConfig.getFiles().get(fileKey);
        log.debug("tailing {}", fileLocation);
        final File file = new File(fileLocation);
        final StringBuilder logTailBuilder = new StringBuilder();
        long linesRead = 0L;
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(file, UTF_8)) {
            String line;
            do {
                line = reader.readLine();

                logTailBuilder.insert(0,
                        line == null ?
                                "=== BEGIN FILE ===\n" :
                                line + "\n");
                linesRead++;
            } while ((line != null) && (linesRead < numOfLines));
        }
        return logTailBuilder.toString();
    }
}
