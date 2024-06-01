package org.workload.generator.mode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.workload.generator.argumentParser.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Throughput implements IGeneratorMode {

    private static final Logger logger = LogManager.getLogger(OneShot.class);

    @Override
    public void execute(Configuration configuration) {
//        threads number is ignored for virtual threads
        try (var executorService = configuration.getVirtualThread() ? Executors.newVirtualThreadPerTaskExecutor()
                : Executors.newFixedThreadPool(configuration.getThreadNumber())){
            execute(configuration, executorService);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void execute(Configuration configuration, ExecutorService executorService) throws InterruptedException {
        final List<Long> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        final HttpClient httpClient = HttpClient.newBuilder().build();
        final List<Callable<String>> tasks = new ArrayList<>();

//        Initialize list of callable tasks
        IntStream.range(0, configuration.getRequestNumber()).forEach(i -> {
            tasks.add(() -> {
                try {
                    final HttpRequest req = HttpRequest
                            .newBuilder()
                            .uri(new URI(configuration.getUrl()))
                            //                fixme: currently empty body is being send with a request
                            .method(configuration.getRequestMethod(), HttpRequest.BodyPublishers.noBody())
                            //                fixme: make variable duration
                            .timeout(Duration.ofSeconds(5))
                            .build();
                    final var start = Instant.now();
                    httpClient.send(req, HttpResponse.BodyHandlers.ofString());
                    final var duration = Duration.between(start, Instant.now()).toMillis();
                    logger.info("Request number {} took in millis: {}", i, duration);
                    copyOnWriteArrayList.add(duration);
                } catch (URISyntaxException | IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
        });
//        blocking method - returns all tass when they are done
        executorService.invokeAll(tasks);

        final var successfulPercent = configuration.getRequestNumber() != 0 ?
                (double) copyOnWriteArrayList.size() / configuration.getRequestNumber() * 100.00 : 0;
        logger.info("Number of successful requests is {} ({}% successful) the average response time {}",
                copyOnWriteArrayList.size(),
                String.format("%.1f", successfulPercent),
                copyOnWriteArrayList.stream().reduce(0L, Long::sum) / copyOnWriteArrayList.size());
    }
}
