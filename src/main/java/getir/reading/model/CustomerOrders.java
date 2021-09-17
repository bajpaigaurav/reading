package getir.reading.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerOrders {
    String orderId;
    String customerName;
    String bookTitle;
    String quantitiesOrdered;
    String author;
    String orderDate;
    String orderStatus;
}
