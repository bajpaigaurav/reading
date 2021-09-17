package getir.reading.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteCustomerResponse {
    private String customerId;
    private String customerName;
    private String status;
}
