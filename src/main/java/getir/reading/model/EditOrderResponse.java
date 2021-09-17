package getir.reading.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditOrderResponse {
    private String orderId;
    private String revisedCopies;
    private String status;
}
