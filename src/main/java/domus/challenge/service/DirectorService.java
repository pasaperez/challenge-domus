package domus.challenge.service;

import domus.challenge.dto.DirectorDTO;
import reactor.core.publisher.Mono;

public interface DirectorService {
  Mono<DirectorDTO> getDirectors(Integer threshold);
}
