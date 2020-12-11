package com.ti.a0284021.monkeytailr;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
public class TailrFileProperties {
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
