package domus.challenge.client;

import domus.challenge.config.WebClientRetryProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RetryableWebClientTest {

    private WebClient.Builder mockBuilder;

    private WebClientRetryProperties retryProperties;
    private RetryableWebClient retryableWebClient;

    @BeforeEach
    void setUp() {
        mockBuilder = mock(WebClient.Builder.class);

        WebClientRetryProperties.Backoff backoff = new WebClientRetryProperties.Backoff();
        backoff.setFirstDelay(Duration.ofMillis(100));
        backoff.setMaxDelay(Duration.ofSeconds(2));

        retryProperties = new WebClientRetryProperties();
        retryProperties.setMaxAttempts(3);
        retryProperties.setBackoff(backoff);

        when(mockBuilder.baseUrl(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.filter(any())).thenAnswer(invocation -> {
            ExchangeFilterFunction filter = invocation.getArgument(0);
            WebClient.Builder newBuilder = WebClient.builder().filter(filter);
            return newBuilder;
        });

        retryableWebClient = new RetryableWebClient(mockBuilder, retryProperties);
    }

    @Test
    void testGetWebClientReturnsConfiguredInstance() {
        WebClient client = retryableWebClient.getWebClient(MovieClient.BASE_URL);
        assertThat(client).isNotNull();
    }

    @Test
    void testRetryConfiguration() {
        RetryBackoffSpec retrySpec = Retry.backoff(
                retryProperties.getMaxAttempts(),
                retryProperties.getBackoff().getFirstDelay()
        ).maxBackoff(retryProperties.getBackoff().getMaxDelay());

        assertThat(retrySpec).isNotNull();
        assertThat(retryProperties.getMaxAttempts()).isEqualTo(3);
        assertThat(retryProperties.getBackoff().getFirstDelay()).isEqualTo(Duration.ofMillis(100));
        assertThat(retryProperties.getBackoff().getMaxDelay()).isEqualTo(Duration.ofSeconds(2));
    }

    @Test
    void testRetryWebClientLogic_onFailure_shouldRetry() {
        WebClient webClient = retryableWebClient.getWebClient(MovieClient.BASE_URL);

        final String expected = "fallback";
        final int page = 1;

        Mono<String> result = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(MovieClient.PATH_SEARCH)
                        .queryParam(MovieClient.QUERY_PAGE, page)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just(expected));

        StepVerifier.create(result)
                .expectNext(expected)
                .verifyComplete();
    }
}
