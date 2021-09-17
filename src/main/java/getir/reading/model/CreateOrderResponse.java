package getir.reading.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderResponse {
    String orderStaus;
    String orderPlacedOn;

}
