package com.ti.a0284021.monkeytailr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FrontendConfigController {
    // TODO: move this to a starter maybe???
    private final TailrFileProperties fileProps;
    private final TailrService tailrService;

    @PostConstruct
    private void init() {
        log.info("Exposing properties to frontend: {}", fileProps);
    }

    @GetMapping("/frontend/config")
    public HttpEntity<TailrFileProperties> config() {

        final Optional<TailrFileProperties> fileProps = Optional.of(this.fileProps);
        fileProps.ifPresent(tailrFileProperties -> {
            tailrFileProperties.setFiles(tailrService.getFileLocationMap());
        });
        return ResponseEntity.of(fileProps);
    }
}

