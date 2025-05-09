package domus.challenge.gateway;

import domus.challenge.model.Movie;
import domus.challenge.dto.MovieDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * MovieGateway establishes communication between our service with IUGO service to fetch movies
 */
@Component
@Slf4j
public class MovieGateway {

  private final WebClient webClient;

  private static final Integer BATCH_SIZE = 10;

  public MovieGateway(WebClient webClient) {
    this.webClient = webClient;
  }

  /**
   * Returns the movies result of consuming IUGO service passing page and perPage parameter
   * @param page number
   * @param perPage quatiny of elements per page
   * @return Mono of MovieDTO.
   */
  public Mono<MovieDTO> getMoviesByPage(Integer page, Integer perPage) {
    try {
      log.info("Consuming IUGO service page: {}", page);
      return webClient.get()
              .uri(uriBuilder -> uriBuilder
                      .path("/movies/search")
                      .queryParam("page", page)
                      .queryParam("per_page", perPage)
                      .build())
              .retrieve()
              .bodyToMono(MovieDTO.class);
    } catch (Exception e) {
      log.error("Something gone wrong consuming IUGO service");
      throw new RuntimeException("An error occurred trying to get movies from IUGO");
    }
  }

  /**
   * Returns a Flux of movies using a batch strategy consuming to avoid depend on the number of pages and the number of movies.
   * @return Flux of Movie.
   */
  public Flux<Movie> fetchAllMovies() {
    return getMoviesByPage(1, BATCH_SIZE)
            .expand(response -> {
              if (response.getData() == null || response.getData().isEmpty()) {
                log.info("No more movies to fetch in page {}", response.getPage());
                return Flux.empty();
              }
              log.info("Consuming page {}", response.getPage() + 1);
              return getMoviesByPage(response.getPage() + 1, BATCH_SIZE);
            })
            .flatMapIterable(MovieDTO::getData);
  }
}
