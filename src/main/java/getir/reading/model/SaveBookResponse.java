package getir.reading.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveBookResponse {
    String bookId;
    String status;
}
