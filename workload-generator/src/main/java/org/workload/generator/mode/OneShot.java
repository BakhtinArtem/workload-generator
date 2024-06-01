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

public class OneShot implements IGeneratorMode {
    private static final Logger logger = LogManager.getLogger(OneShot.class);

    @Override
    public void execute(Configuration configuration) {
        try {
            final var req = HttpRequest
                    .newBuilder()
                    .uri(new URI(configuration.getUrl()))
//                    fixme: currently empty body is being send with a request
                    .method(configuration.getRequestMethod(), HttpRequest.BodyPublishers.noBody())
                    .build();
            Instant start = Instant.now();
//            ignore result for now
            HttpClient.newBuilder().build().send(req, HttpResponse.BodyHandlers.ofString());
            logger.info("Request time in millis: {}", Duration.between(start, Instant.now()).toMillis());
        } catch (URISyntaxException ex) {
            throw new RuntimeException("Not valid uri.");
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
