package domus.challenge.dto;

import lombok.Data;

import java.util.List;

@Data
public class DirectorResponse {
    private List<String> directors;

    public DirectorResponse(List<String> directors) {
        this.directors = directors;
    }
}
