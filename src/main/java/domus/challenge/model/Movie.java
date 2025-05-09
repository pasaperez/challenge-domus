package domus.challenge.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class Movie {

  private String title;
  private String year;
  private String rated;
  private String released;
  private String runtime;
  private String genre;
  private String director;
  private String writer;
  private String actors;

}
