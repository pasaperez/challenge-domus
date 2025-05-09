package domus.challenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import domus.challenge.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
  private Integer page;
  @JsonProperty("per_page")
  private Integer perPage;
  private Integer total;
  @JsonProperty("total_pages")
  private Integer totalPages;
  private List<Movie> data;
}
