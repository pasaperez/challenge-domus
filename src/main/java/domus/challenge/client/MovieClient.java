package domus.challenge.client;

import domus.challenge.model.MoviePageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class MovieClient {

    public static final String BASE_URL = "https://challenge.iugolabs.com";
    public static final String PATH_SEARCH = "/api/movies/search";
    public static final String QUERY_PAGE = "page";

    private final RetryableWebClient retryableWebClient;

    public Mono<MoviePageResponse> fetchMoviesPage(int page) {
        WebClient client = retryableWebClient.getWebClient(BASE_URL);

        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH_SEARCH)
                        .queryParam(QUERY_PAGE, page)
                        .build()
                )
                .retrieve()
                .bodyToMono(MoviePageResponse.class)
                .doOnError(e -> log.error("Failed to fetch movies page {}: {}", page, e.getMessage(), e));
    }
}
