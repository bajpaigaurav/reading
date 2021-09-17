package getir.reading.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterCustomerResponse {
    private String customerId;
    private String status;
}
