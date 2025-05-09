package domus.challenge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response containing list of directors")
public class DirectorsResponse {

    @Schema(description = "Sorted list of director names", example = "[\"Martin Scorsese\", \"Woody Allen\"]")
    private List<String> directors;
}