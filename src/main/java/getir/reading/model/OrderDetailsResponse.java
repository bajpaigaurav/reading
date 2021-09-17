package getir.reading.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderDetailsResponse {
    private String orderId;
    private String title;
    private String author;
    private String customerName;
    private String copiesOrdered;
    private String orderPlacedOn;
}
