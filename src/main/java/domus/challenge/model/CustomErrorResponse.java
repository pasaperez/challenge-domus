package domus.challenge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Error response")
public class CustomErrorResponse {

    @Schema(description = "Short description of the error", example = "Bad Request")
    private String error;

    @Schema(description = "Detailed message explaining the error", example = "Invalid threshold: abc")
    private String message;

    @Schema(description = "Timestamp of the error occurrence", example = "2025-05-07T15:30:00Z")
    private Instant timestamp;
}