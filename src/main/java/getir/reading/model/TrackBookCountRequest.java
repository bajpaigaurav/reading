package getir.reading.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TrackBookCountRequest {

    @NotBlank(message = "Title is not valid")
    private String title;
    @NotBlank(message = "Author name is not valid")
    private String author;
}
