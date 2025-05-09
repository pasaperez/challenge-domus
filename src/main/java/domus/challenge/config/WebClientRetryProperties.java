package domus.challenge.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties(prefix = "webclient.retry")
public class WebClientRetryProperties {
    private int maxAttempts;
    private Backoff backoff = new Backoff();

    @Data
    public static class Backoff {
        private Duration firstDelay;
        private Duration maxDelay;
    }
}
