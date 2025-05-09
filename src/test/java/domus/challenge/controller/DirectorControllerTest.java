package domus.challenge.controller;

import domus.challenge.dto.DirectorDTO;
import domus.challenge.service.DirectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DirectorControllerTest {

  private WebTestClient webTestClient;
  private DirectorService directorService;

  @BeforeEach
  void setUp() {
    directorService = mock(DirectorService.class);
    webTestClient = WebTestClient.bindToController(new DirectorController(directorService)).build();
  }

  @Test
  void getDirectors() {
    DirectorDTO response = new DirectorDTO();
    when(directorService.getDirectors(3)).thenReturn(Mono.just(response));

    webTestClient.get()
            .uri("/api/directors?threshold=3")
            .exchange()
            .expectStatus().isOk()
            .expectBody(DirectorDTO.class).isEqualTo(response);
  }

  @Test
  void getDirectorsNegativeThreshold() {
    DirectorDTO response = new DirectorDTO(new ArrayList<>());
    when(directorService.getDirectors(-3)).thenReturn(Mono.just(response));

    webTestClient.get()
            .uri("/api/directors?threshold=-3")
            .exchange()
            .expectStatus().isOk()
            .expectBody(DirectorDTO.class).isEqualTo(response);
  }

  @Test
  void getDirectorsNoParameter() {
    DirectorDTO response = new DirectorDTO();
    when(directorService.getDirectors(3)).thenReturn(Mono.just(response));

    webTestClient.get()
            .uri("/api/directors")
            .exchange()
            .expectStatus().isBadRequest();
  }

}