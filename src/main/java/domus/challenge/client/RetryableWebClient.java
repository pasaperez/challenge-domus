package domus.challenge.client;

import domus.challenge.config.WebClientRetryProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryableWebClient {
    private final WebClient.Builder webClientBuilder;
    private final WebClientRetryProperties retryProperties;

    public WebClient getWebClient(String baseUrl) {
        return webClientBuilder
                .baseUrl(baseUrl)
                .filter((request, next) -> next.exchange(request)
                        .retryWhen(Retry.backoff(
                                        retryProperties.getMaxAttempts(),
                                        retryProperties.getBackoff().getFirstDelay()
                                )
                                .maxBackoff(retryProperties.getBackoff().getMaxDelay())
                                .doBeforeRetry(retrySignal ->
                                        log.warn("Retrying request: attempt={}, reason={}",
                                                retrySignal.totalRetries() + 1,
                                                retrySignal.failure().getMessage())
                                ))
                )
                .build();
    }
}
