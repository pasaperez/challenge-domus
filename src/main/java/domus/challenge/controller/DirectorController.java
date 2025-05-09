package domus.challenge.controller;

import domus.challenge.dto.DirectorDTO;
import domus.challenge.service.DirectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
  * DirectorController is the controller layer, provides Director endpoints related
 */
@RestController
@RequestMapping("/api/directors")
public class DirectorController {

  @Autowired
  private DirectorService directorService;

  public DirectorController(DirectorService directorService) {
    this.directorService = directorService;
  }


  /**
   * Provides a list of names of directors who have directed more than threshold number of films.
   * The list is ordered alphabetically.
   * @param threshold param
   * @return Mono of DirectorDTO.
   */
  @Operation(summary = "Fetch directors names", description = "Provides a list of names of directors who have directed more than threshold number of films. " +
          "The list is ordered alphabetically")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "OK"),
          @ApiResponse(responseCode = "400", description = "Bad request"),
          @ApiResponse(responseCode = "404", description = "Not Found")
  })
  @GetMapping
  public Mono<DirectorDTO> getDirectors(@RequestParam Integer threshold) {
    return directorService.getDirectors(threshold);
  }
}
