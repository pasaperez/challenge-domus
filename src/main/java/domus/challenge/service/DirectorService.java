package domus.challenge.service;

import domus.challenge.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DirectorService {

    private final MovieService movieService;

    @Autowired
    public DirectorService(MovieService movieService) {
        this.movieService = movieService;
    }

    public Mono<List<String>> getDirectorsWithMoreMovies(int threshold) {
        return movieService.getAllMovies()
                .map(movies -> movies.stream()
                        .collect(Collectors.groupingBy(Movie::getDirector, Collectors.counting())))
                .map(moviesPerDirector -> moviesPerDirector.entrySet().stream()
                        .filter(entry -> entry.getValue() > threshold)
                        .map(Map.Entry::getKey)
                        .sorted()
                        .collect(Collectors.toList()));
    }
}
