package domus.challenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import domus.challenge.model.Movie;
import lombok.Data;

import java.util.List;

@Data
public class MovieResponse {
    @JsonProperty("page")
    private int page;
    @JsonProperty("per_page")
    private int perPage;
    @JsonProperty("total")
    private int total;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("data")
    private List<Movie> data;
}
