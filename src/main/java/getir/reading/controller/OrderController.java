package getir.reading.controller;

import getir.reading.model.*;
import getir.reading.service.OrderDetailsService;
import getir.reading.validations.RequestValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Validated
@RestController
@Slf4j
@RequestMapping("/getir/order")
public class OrderController {

    @Autowired
    OrderDetailsService orderDetailsService;

    @Autowired
    RequestValidation requestValidation;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse> createOrder(@Valid
                                                       @RequestBody CreateOrderRequest createOrderRequest) {

        log.info("In order controller to create new order for customer Id: {}",
                createOrderRequest.getCustomerId());
        CreateOrderResponse createOrder = orderDetailsService.createOrder(createOrderRequest);
        SuccessResponse<CreateOrderResponse> createOrderSuccess = new SuccessResponse<>();
        createOrderSuccess.setData(createOrder);
        return new ResponseEntity<>(createOrderSuccess, HttpStatus.CREATED);
    }

    @PutMapping("/edit")
    public ResponseEntity<SuccessResponse> editOrder(@Valid
                                                     @RequestBody EditOrderRequest editORderRequest) {
        log.info("In order controller to edit order with order Id: {}",
                editORderRequest.getOrderId());
        EditOrderResponse editOrderResponse = orderDetailsService.editOrder(editORderRequest);
        SuccessResponse<EditOrderResponse> editOrderSuccess = new SuccessResponse<>();
        editOrderSuccess.setData(editOrderResponse);
        return new ResponseEntity<>(editOrderSuccess, HttpStatus.OK);
    }

    @GetMapping("/details/id")
    public ResponseEntity<SuccessResponse> displayOrderDetailsById(@RequestParam @NotBlank String orderId) {
        log.info("In order controller to fetch order details with order Id: {}", orderId);
        OrderDetailsResponse orderDetailsResponse = orderDetailsService.displayOrderDetailsById(orderId);
        SuccessResponse<OrderDetailsResponse> orderDetailsSuccess = new SuccessResponse<>();
        orderDetailsSuccess.setData(orderDetailsResponse);
        return new ResponseEntity<>(orderDetailsSuccess, HttpStatus.FOUND);
    }

    @PostMapping("/details/timeInterval")
    public ResponseEntity<SuccessResponse> displayOrderDetailsInTimeInterval(
            @Valid @RequestBody OrderDetailsIntervalRequest orderDetailsInterval,
            @RequestParam(name = "pageNumber") int pageNumber,
            @RequestParam(name = "recordCount") int recordCount) {
        log.info("In order controller to fetch all orders between time interval: {} and {}",
                orderDetailsInterval.getStartTime(), orderDetailsInterval.getEndTime());
        requestValidation.validateOrderDetailsWithTimeRequest(orderDetailsInterval);
        SuccessResponse orderDetailsList =
                orderDetailsService.orderDetailsByTimeInterval(orderDetailsInterval,
                        pageNumber, recordCount);
        return new ResponseEntity<>(orderDetailsList, HttpStatus.PARTIAL_CONTENT);
    }

    @GetMapping("details/all")
    public ResponseEntity<SuccessResponse> getAllOrders(@RequestParam("customerId") String customerId,
                                                        @RequestParam(name = "pageNumber") int pageNumber,
                                                        @RequestParam(name = "recordCount") int recordCount) {
        log.info("In order controller to fetch all orders for customer Id: {}", customerId);
        SuccessResponse successResponse = orderDetailsService.getAllOrders(customerId, pageNumber, recordCount);
        return new ResponseEntity<>(successResponse, HttpStatus.FOUND);
    }
}
