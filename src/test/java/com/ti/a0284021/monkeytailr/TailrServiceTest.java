package com.ti.a0284021.monkeytailr;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
@Slf4j
class TailrServiceTest {

    TailrService instance;

    @Mock
    SimpMessagingTemplate mockTemplate;

    @BeforeEach
    void setUp() {
        final TailrFileProperties fileConfig = new TailrFileProperties();
        fileConfig.setFiles(ImmutableMap.of("fileKey", "testFixtures/EULA.md"));
        instance = new TailrService(fileConfig, mockTemplate);
    }

    @Test
    void getTail() throws IOException {
        final String fileTail = instance.getTail("fileKey", 5L);

        log.info("got fileTail{}", fileTail);

        assertThat(fileTail, is("risus ac congue. Donec quis ipsum ipsum. Morbi tincidunt sed erat quis varius. Nam\n" +
                "viverra non libero vitae sagittis. In hac habitasse platea dictumst. Morbi dapibus\n" +
                "lacus vel ante eleifend, eu vulputate dolor facilisis. Quisque vitae odio tellus.\n" +
                "Cras mi sem, pretium tempor nunc tristique, iaculis tempor nisl. Duis rhoncus velit\n" +
                "quis lorem semper lacinia.\n"));
    }
}