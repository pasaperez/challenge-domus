package domus.challenge.service;

import domus.challenge.dto.DirectorDTO;
import domus.challenge.gateway.MovieGateway;
import domus.challenge.model.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

/**
 * DirectorService is the service layer related with Directors
 */
@Service
@Slf4j
public class DirectorServiceImpl implements DirectorService {

  @Autowired
  private final MovieGateway movieGateway;

  public DirectorServiceImpl(MovieGateway movieGateway) {
    this.movieGateway = movieGateway;
  }

  /**
   * Provides a list of names of directors who have directed more than threshold number of films.
   * The list is ordered alphabetically
   * @param threshold param
   * @return Mono of DirectorDTO.
   */
    public Mono<DirectorDTO> getDirectors(Integer threshold) {
      if (threshold < 0) {
        log.warn("Received 0 like parameter");
        return Mono.just(new DirectorDTO(new ArrayList<>()));
      }
      log.info("Fetching movies from IUGO");
      return movieGateway.fetchAllMovies()
              .groupBy(Movie::getDirector)
              .flatMap(group -> group.count()
                      .filter(quantity -> quantity > threshold)
                      .map(v -> group.key()))
              .sort()
              .collectList()
              .map(DirectorDTO::new);
    }
}
