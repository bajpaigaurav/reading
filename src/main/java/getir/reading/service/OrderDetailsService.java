package getir.reading.service;

import getir.reading.model.*;
import org.springframework.stereotype.Component;

@Component
public interface OrderDetailsService {

    CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest);

    EditOrderResponse editOrder(EditOrderRequest editORderRequest);

    OrderDetailsResponse displayOrderDetailsById(String orderId);

    SuccessResponse orderDetailsByTimeInterval(
            OrderDetailsIntervalRequest orderDetailsInterval, int pageNumber,
            int recordCount);

    SuccessResponse getAllOrders(String customerId, int pageNumber, int recordCount);
}
