package exercise;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ThreadTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ThreadTask.class.getName());
    private static final String OUT_FOLDER  = "D:/test/";
    private final URL url;

    public ThreadTask(URL url) {
        this.url  = url;
    }

    @Override
    public void run() {
        log.info("Begin downloading {}", url);
        String fileName = getFileName(url);
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream())) {
            File targetFile = new File(OUT_FOLDER + fileName);
            Files.copy(bufferedInputStream, targetFile.toPath());
        } catch (IOException e) {
            log.error("File copy error", e);
        }
        log.info("End downloading {}", url);
    }

    private String getFileName(URL url) {
        String urlStr = url.toString();
        return urlStr.substring(urlStr.lastIndexOf('/') + 1);
    }
}
