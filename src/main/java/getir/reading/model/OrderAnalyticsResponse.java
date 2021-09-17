package getir.reading.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderAnalyticsResponse {
    private String month;
    private String totalOrderCount;
    private String totalBookCount;
    private String totalPurchasedAmount;
    private String customerId;

}
