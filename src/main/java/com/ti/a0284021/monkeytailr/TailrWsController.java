package com.ti.a0284021.monkeytailr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;

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

    @GetMapping("/tail")
    @ResponseBody
    public HttpEntity<String> tail(
            @RequestParam("fileKey") String fileKey,
            @RequestParam(value = "numOfLines", required = false) String numOfLinesStr) throws IOException {
        long numOfLines = 1000L;
        if (!StringUtils.isEmpty(numOfLinesStr)) try {
            numOfLines = Long.parseLong(numOfLinesStr);
        } catch (NumberFormatException e) {
            log.warn("numOfLines: Encountered wrong number format. Falling back to default = {}", numOfLines, e);
        }

        return ResponseEntity.ok(tailrService.getTail(fileKey, numOfLines));
    }

    @GetMapping("/download/logFile")
    public void downloadLog(
            @RequestParam("fileKey") String fileKey,
            HttpServletResponse response) throws IOException {
        File logFile = tailrService.getLogFile(fileKey);

        String mimeType = URLConnection.guessContentTypeFromName(logFile.getName());
        if (mimeType == null) {
            //unknown mimetype so set the mimetype to application/octet-stream
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + logFile.getName() + "\""));
        InputStream inputStream = new BufferedInputStream(new FileInputStream(logFile));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
}
