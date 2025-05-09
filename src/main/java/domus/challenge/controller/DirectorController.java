package domus.challenge.controller;

import domus.challenge.exception.InvalidThresholdException;
import domus.challenge.model.CustomErrorResponse;
import domus.challenge.model.DirectorsResponse;
import domus.challenge.service.DirectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/directors")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @Operation(
            summary = "Get directors above a movie count threshold",
            description = "Returns directors with more than 'threshold' movies released after 2010.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of directors",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DirectorsResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid threshold value",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomErrorResponse.class))
                    )
            }
    )
    @GetMapping
    public Mono<DirectorsResponse> getDirectorsAboveThreshold(@RequestParam String threshold) {
        int parsedThreshold;
        try {
            parsedThreshold = Integer.parseInt(threshold);
        } catch (NumberFormatException e) {
            throw new InvalidThresholdException("Invalid threshold: '" + threshold + "'");
        }
        return directorService.getDirectorsAboveThreshold(parsedThreshold);
    }
}
