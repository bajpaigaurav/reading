package getir.reading.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FindBookResponse {

    String title;
    String author;
    int availableCopies;

}
