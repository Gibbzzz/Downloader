package exercise;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Downloader {

    private static final Logger log = LoggerFactory.getLogger(Downloader.class.getName());
    private static final String FILE_NAME = "D:/1.txt";

    public void download() {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        try {
            List<URL> listURL = this.getURLList();
            CompletableFuture<?>[] futures = listURL.stream()
                    .map(url -> CompletableFuture.runAsync(new ThreadTask(url), executor))
                    .toArray(CompletableFuture<?>[]::new);
            CompletableFuture.allOf(futures).join();
        } catch (IOException e) {
            log.error("File read error", e);
        } finally {
            executor.shutdown();
        }
    }

    private List<URL> getURLList () throws IOException {
        List<URL> listOfURLs = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME)))) {
            while (bufferedReader.ready()) {
                listOfURLs.add(new URL(bufferedReader.readLine()));
            }
        }
        return listOfURLs;
    }
}
