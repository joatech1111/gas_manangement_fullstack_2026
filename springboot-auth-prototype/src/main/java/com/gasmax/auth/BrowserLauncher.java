package com.gasmax.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URI;

@Slf4j
@Component
public class BrowserLauncher implements ApplicationListener<ApplicationReadyEvent> {

    private final Environment env;

    public BrowserLauncher(Environment env) {
        this.env = env;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String port = env.getProperty("server.port", "8081");
        String url = "http://localhost:" + port;
        openBrowser(url);
    }

    private void openBrowser(String url) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
                log.info("브라우저 실행: {}", url);
            } else {
                // macOS / Linux fallback
                String os = System.getProperty("os.name").toLowerCase();
                Runtime rt = Runtime.getRuntime();
                if (os.contains("mac")) {
                    rt.exec(new String[]{"open", url});
                } else if (os.contains("linux")) {
                    rt.exec(new String[]{"xdg-open", url});
                } else if (os.contains("win")) {
                    rt.exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", url});
                }
                log.info("브라우저 실행 (fallback): {}", url);
            }
        } catch (Exception e) {
            log.warn("브라우저 자동 실행 실패: {}", e.getMessage());
        }
    }
}
