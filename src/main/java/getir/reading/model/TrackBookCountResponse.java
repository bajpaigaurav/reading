package getir.reading.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TrackBookCountResponse {

    private String availableCopies;
}
