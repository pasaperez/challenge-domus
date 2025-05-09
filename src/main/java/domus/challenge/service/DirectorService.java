package domus.challenge.service;

import domus.challenge.client.MovieClient;
import domus.challenge.model.DirectorsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {

    private final MovieClient movieClient;

    public Mono<DirectorsResponse> getDirectorsAboveThreshold(int threshold) {
        if (threshold < 0) {
            return Mono.just(new DirectorsResponse(List.of()));
        }

        Map<String, Integer> directorCounts = new ConcurrentHashMap<>();
        return fetchAndCount(1, directorCounts)
                .then(Mono.fromCallable(() ->
                        directorCounts.entrySet().stream()
                                .filter(entry -> entry.getValue() > threshold)
                                .map(Map.Entry::getKey)
                                .sorted()
                                .toList()
                ))
                .map(DirectorsResponse::new);
    }

    private Mono<Void> fetchAndCount(int page, Map<String, Integer> directorCounts) {
        return movieClient.fetchMoviesPage(page)
                .flatMap(moviePage -> {
                    moviePage.getData().forEach(movie -> {
                        String director = movie.getDirector();
                        if (director != null && !director.isBlank()) {
                            directorCounts.merge(director.trim(), 1, Integer::sum);
                        }
                    });

                    if (page < moviePage.getTotalPages()) {
                        return fetchAndCount(page + 1, directorCounts);
                    } else {
                        return Mono.empty();
                    }
                });
    }
}
