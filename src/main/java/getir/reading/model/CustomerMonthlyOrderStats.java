package getir.reading.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerMonthlyOrderStats {
    private String month;
    private long totalOrderCount;
    private long totalBookCount;
    private double totalPurchasedAmount;
    private int customerId;
}
