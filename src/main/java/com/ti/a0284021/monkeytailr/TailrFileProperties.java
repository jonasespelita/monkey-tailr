package com.ti.a0284021.monkeytailr;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "tailr")
public class TailrFileProperties {
    private Map<String, String> files = new HashMap<>();

}
