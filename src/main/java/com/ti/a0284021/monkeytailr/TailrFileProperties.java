package com.ti.a0284021.monkeytailr;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


/**
 * Properties loaded from context for tailr.
 *
 * @author a0284021
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "tailr")
@ToString
@Slf4j
public class TailrFileProperties {

    @PostConstruct
    private void postConstruct() {
        log.info("Loaded tailr properties {}", this.toString());
    }

    /**
     * file key - file location map key values.
     */
    private Map<String, String> files = new HashMap<>();

    /**
     * web socket broker url to pass to UI.
     */
    private String brokerUrl;

    /**
     * Path location to scan for files to expose to websocket.
     */
    private String path;

    //language=RegExp
    /**
     * Regex pattern to match. Defaults to any.
     */
    private String filePatternRegex = "[\\w]*";
}
