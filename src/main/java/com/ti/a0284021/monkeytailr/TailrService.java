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
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Tailr core service.
 *
 * @author j-espelita@ti.com
 */
@SuppressWarnings("WeakerAccess")
@Service
@RequiredArgsConstructor
@Slf4j
public class TailrService implements DisposableBean {

    private final TailrFileProperties fileConfig;
    private final SimpMessagingTemplate simpMessagingTemplate;


    private final Map<String, Tailer> fileTailerMap = new ConcurrentHashMap<>();
    private final Map<String, String> fileLocationMap = new ConcurrentHashMap<>();

    /**
     * @return fileLocationMap
     */
    public Map<String, String> getFileLocationMap() {
        return fileLocationMap;
    }

    /**
     * Service initializer that starts tailing configured files.
     */
    @PostConstruct
    void init() throws IOException {
        // start tailing files and publishing appropriately
        fileConfig.getFiles()
                .forEach(this::tailFile);

        // scan path property and start publishing appropriately
        try (Stream<Path> paths = Files.walk(Paths.get(fileConfig.getPath()), 1, FileVisitOption.FOLLOW_LINKS)) {
            paths.filter(Files::isRegularFile)
                    .peek(path -> log.debug("matching file {} to {}", path.getFileName().toString(), fileConfig.getFilePatternRegex()))
                    .filter(path -> path.getFileName().toString()
                            .matches(fileConfig.getFilePatternRegex()))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .forEach(location -> this.tailFile(UUID.randomUUID().toString(), location));
        }

        log.info("Loaded files {}", fileLocationMap);
    }

    private void tailFile(String fileKey, String location) {
        final Tailer myTailer = Tailer.create(
                new File(location),
                UTF_8,
                new MonkeyTailrListener(simpMessagingTemplate, fileKey),
                1000,
                true, false, 4096);

        fileTailerMap.put(
                fileKey,
                myTailer);
        fileLocationMap.put(fileKey, location);
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

    /**
     * Retrieves a file from file keys.
     *
     * @param fileKey file key to retrieve
     * @return matching file for file key
     */
    public File getLogFile(String fileKey) {
        return new File(fileConfig.getFiles().get(fileKey));
    }
}
