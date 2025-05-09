package domus.challenge.service;

import domus.challenge.client.MovieClient;
import domus.challenge.model.Movie;
import domus.challenge.model.MoviePageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

class DirectorServiceTest {

    private MovieClient movieClient;
    private DirectorService directorService;

    @BeforeEach
    void setUp() {
        movieClient = mock(MovieClient.class);
        directorService = new DirectorService(movieClient);
    }

    @Test
    void getDirectorsAboveThreshold_shouldReturnDirectorsAboveThreshold() {
        Movie m1 = new Movie();
        m1.setDirector("Christopher Nolan");
        Movie m2 = new Movie();
        m2.setDirector("Christopher Nolan");
        Movie m3 = new Movie();
        m3.setDirector("Martin Scorsese");

        MoviePageResponse page1 = new MoviePageResponse();
        page1.setPage(1);
        page1.setTotalPages(2);
        page1.setData(List.of(m1, m2, m3));

        Movie m4 = new Movie();
        m4.setDirector("Christopher Nolan");
        Movie m5 = new Movie();
        m5.setDirector("Quentin Tarantino");
        MoviePageResponse page2 = new MoviePageResponse();
        page2.setPage(2);
        page2.setTotalPages(2);
        page2.setData(List.of(m4, m5));

        when(movieClient.fetchMoviesPage(1)).thenReturn(Mono.just(page1));
        when(movieClient.fetchMoviesPage(2)).thenReturn(Mono.just(page2));

        StepVerifier.create(directorService.getDirectorsAboveThreshold(2))
                .expectNextMatches(response ->
                        response.getDirectors().equals(List.of("Christopher Nolan"))
                )
                .verifyComplete();

        verify(movieClient).fetchMoviesPage(1);
        verify(movieClient).fetchMoviesPage(2);
    }

    @Test
    void getDirectorsAboveThreshold_shouldReturnEmptyListIfThresholdIsNegative() {
        StepVerifier.create(directorService.getDirectorsAboveThreshold(-10))
                .expectNextMatches(response -> response.getDirectors().isEmpty())
                .verifyComplete();

        verifyNoInteractions(movieClient);
    }

    @Test
    void getDirectorsAboveThreshold_shouldReturnEmptyListIfNoDirectorsPassThreshold() {
        Movie m1 = new Movie();
        m1.setDirector("Director A");
        Movie m2 = new Movie();
        m2.setDirector("Director B");
        MoviePageResponse page = new MoviePageResponse();
        page.setPage(1);
        page.setTotalPages(1);
        page.setData(List.of(m1, m2));

        when(movieClient.fetchMoviesPage(1)).thenReturn(Mono.just(page));

        StepVerifier.create(directorService.getDirectorsAboveThreshold(1))
                .expectNextMatches(response -> response.getDirectors().isEmpty())
                .verifyComplete();
    }
}
