package domus.challenge.controller;

import domus.challenge.controller.advice.GlobalExceptionHandler;
import domus.challenge.model.DirectorsResponse;
import domus.challenge.service.DirectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class DirectorControllerTest {

    private DirectorService directorService;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        directorService = mock(DirectorService.class);

        DirectorController controller = new DirectorController(directorService);
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        webTestClient = WebTestClient.bindToController(controller)
                .controllerAdvice(handler)
                .build();
    }

    @Test
    void getDirectorsAboveThreshold_shouldReturnDirectors() {
        List<String> directors = List.of("Nolan", "Scorsese");
        DirectorsResponse response = new DirectorsResponse(directors);

        when(directorService.getDirectorsAboveThreshold(3)).thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/api/directors?threshold=3")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DirectorsResponse.class)
                .value(body -> {
                    assert body.getDirectors().equals(directors);
                });

        verify(directorService).getDirectorsAboveThreshold(3);
    }

    @Test
    void getDirectorsAboveThreshold_shouldReturnBadRequestForInvalidInput() {
        webTestClient.get()
                .uri("/api/directors?threshold=abc")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Bad Request")
                .jsonPath("$.message").value(msg -> ((String) msg).contains("Invalid threshold"));
    }

    @Test
    void getDirectorsAboveThreshold_shouldHandleNegativeThreshold() {
        DirectorsResponse emptyResponse = new DirectorsResponse(List.of());

        when(directorService.getDirectorsAboveThreshold(-5)).thenReturn(Mono.just(emptyResponse));

        webTestClient.get()
                .uri("/api/directors?threshold=-5")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DirectorsResponse.class)
                .value(response -> {
                    assert response.getDirectors().isEmpty();
                });

        verify(directorService).getDirectorsAboveThreshold(-5);
    }
}
