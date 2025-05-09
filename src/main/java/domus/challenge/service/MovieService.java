package domus.challenge.service;

import domus.challenge.dto.MovieResponse;
import domus.challenge.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
public class MovieService {
    private final WebClient webClient;

    @Autowired
    public MovieService(WebClient customWebClient) {
        this.webClient = customWebClient;
    }

    public Mono<List<Movie>> getAllMovies() {
        return getMovieResponseForPage(1)
                .flatMap(firstResponse -> {
                    int totalPages = (firstResponse != null)
                            ? firstResponse.getTotalPages()
                            : 1;

                    return Flux.range(1, totalPages)
                            .flatMap(this::getMovieResponseForPage)
                            .flatMapIterable(response -> response != null && response.getData() != null ? response.getData() : Collections.emptyList())
                            .collectList();
                });
    }

    private Mono<MovieResponse> getMovieResponseForPage(int page) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/movies/search")
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("Error getting response: " + response.statusCode() + " - " + errorBody))))
                .bodyToMono(MovieResponse.class)
                .onErrorResume(e -> {
                    System.err.println("Error when trying to connect to external service: " + e.getMessage());
                    return Mono.empty();
                });
    }
}
