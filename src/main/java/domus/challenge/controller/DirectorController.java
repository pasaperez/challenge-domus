package domus.challenge.controller;

import domus.challenge.dto.DirectorResponse;
import domus.challenge.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/directors")
public class DirectorController {
    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public Mono<DirectorResponse> getDirectorsWithMoreMovies(@RequestParam(value = "threshold", defaultValue = "1") int threshold) {
        return this.directorService.getDirectorsWithMoreMovies(threshold)
                .map(DirectorResponse::new);
    }
}
