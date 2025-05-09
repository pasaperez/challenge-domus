package domus.challenge.gateway;

import domus.challenge.dto.MovieDTO;
import domus.challenge.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.function.Function;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieGatewayTest {

  private WebClient webClient;
  private WebClient.Builder webClientBuilder;
  private MovieGateway movieGateway;
  private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
  private WebClient.ResponseSpec responseSpec;


  @BeforeEach
  public void setUp() {
    webClient = mock(WebClient.class);
    webClientBuilder = mock(WebClient.Builder.class);
    requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
    responseSpec = mock(WebClient.ResponseSpec.class);

    movieGateway = new MovieGateway(webClient);
  }

  @Test
  void testGetAllMovies() {
    MovieDTO movieDTO = new MovieDTO();
    movieDTO.setData(List.of(generateMovie()));
    Mono<MovieDTO> response = Mono.just(movieDTO);

    when(webClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.bodyToMono(MovieDTO.class)).thenReturn(response);

    Mono<MovieDTO> result = movieGateway.getMoviesByPage(1, 10);

    StepVerifier.create(result)
            .expectNext(movieDTO)
            .verifyComplete();
  }

  @Test
  void getMoviesByPage() {
    MovieDTO movieDTO = new MovieDTO();
    movieDTO.setData(List.of(generateMovie()));
    Mono<MovieDTO> response = Mono.just(movieDTO);

    when(webClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.bodyToMono(MovieDTO.class)).thenReturn(response);

    Mono<MovieDTO> result = movieGateway.getMoviesByPage(-1, 10);

    StepVerifier.create(result)
            .expectNext(movieDTO)
            .verifyComplete();
  }
  private Movie generateMovie() {
    Movie movie = new Movie();
    movie.setTitle("Kill Bill: The Whole Bloody Affair");
    movie.setYear("2011");
    movie.setReleased("27 Mar 2011");
    movie.setRuntime("247 min");
    movie.setGenre("Action, Crime, Thriller");
    movie.setRated("Not Rated");
    movie.setDirector("Quentin Tarantino");
    movie.setWriter("Quentin Tarantino, Uma Thurman");
    movie.setActors("Uma Thurman, Vivica A. Fox, Michael Madsen");
    return movie;
  }
}