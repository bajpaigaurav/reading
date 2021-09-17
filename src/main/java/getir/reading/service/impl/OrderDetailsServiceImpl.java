package getir.reading.service.impl;

import getir.reading.Exception.*;
import getir.reading.constants.ReadingAppConstants;
import getir.reading.dao.BookDetails;
import getir.reading.dao.CustomerDetails;
import getir.reading.dao.OrderDetails;
import getir.reading.model.*;
import getir.reading.repository.BookDetailsRepository;
import getir.reading.repository.CustomerDetailsRepository;
import getir.reading.repository.OrderDetailsRepository;
import getir.reading.service.OrderDetailsService;
import getir.reading.util.ReadingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    @Autowired
    BookDetailsRepository bookDetailsRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Autowired
    CustomerDetailsRepository customerDetailsRepository;

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) {
        Optional<BookDetails> bookDetails = bookDetailsRepository
                .findByTitleAndAuthorAndAvailableCopiesGreaterThan(createOrderRequest.getTitle(),
                        createOrderRequest.getAuthor(), 1);

        if (bookDetails.isEmpty()) {
            log.info("Book with title {} by author {} not found in the DB",
                    createOrderRequest.getTitle(), createOrderRequest.getAuthor());
            throw BooksNotFoundException.builder().build();
        }

        Optional<CustomerDetails> customerDetails = customerDetailsRepository
                .findById(Integer.valueOf(createOrderRequest.getCustomerId()));

        if (customerDetails.isEmpty()) {
            log.info("Customer with Id not registered", createOrderRequest.getCustomerId());
            throw CustomerNotFoundException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        CreateOrderResponse createOrderResponse;
        log.info("Initiating place order flow...");
        createOrderResponse = placeOrder(bookDetails.get(),
                customerDetails.get(), createOrderRequest);
        log.info("Order placed successfully");
        return createOrderResponse;
    }

    @Transactional
    private synchronized CreateOrderResponse placeOrder(
            BookDetails bookDetails, CustomerDetails customerDetails, CreateOrderRequest createOrderRequest) {

        if (Integer.parseInt(createOrderRequest.getCopiesOrdered())
                > bookDetails.getAvailableCopies()) {
            log.info("Requested copies of book Id: {} not available", bookDetails.getId());
            throw RequestedQuantityNotAvailable.builder().build();
        }

        OrderDetails orderDetails = OrderDetails.builder()
                .quantityOrdered(Integer.parseInt(createOrderRequest.getCopiesOrdered()))
                .orderPlacedOn(ReadingUtils.getCurrentTimestamp())
                .orderStatus(ReadingAppConstants.ORDER_PLACED)
                .totalAmount(bookDetails.getPrice()
                        * Integer.parseInt(createOrderRequest.getCopiesOrdered()))
                .bookDetails(bookDetails)
                .customerDetails(customerDetails)
                .month(new SimpleDateFormat("MMMM").format(new Date()))
                .build();

        orderDetailsRepository.save(orderDetails);
        log.info("Order placed successfully with Order Id: {}", orderDetails.getId());

        log.info("Updating available copies for book Id: {} after order is placed",
                bookDetails.getId());
        bookDetails.setAvailableCopies(bookDetails.getAvailableCopies()
                - Integer.parseInt(createOrderRequest.getCopiesOrdered()));
        bookDetails.setLastUpdatedOn(ReadingUtils.getCurrentTimestamp());
        bookDetailsRepository.save(bookDetails);
        log.info("Available copies count updated successfully");

        return CreateOrderResponse.builder()
                .orderStaus(ReadingAppConstants.ORDER_PLACED)
                .orderPlacedOn(ReadingUtils.getCurrentTimeString())
                .build();
    }

    @Override
    public EditOrderResponse editOrder(EditOrderRequest editOrderRequest) {
        log.info("Fetching order details with order Id: {}", editOrderRequest.getOrderId());
        Optional<OrderDetails> orderDetails = orderDetailsRepository
                .findById(Integer.valueOf(editOrderRequest.getOrderId()));
        if (orderDetails.isEmpty()) {
            log.info("Order with id: {} was not found", editOrderRequest.getOrderId());
            throw OrderNotFoundException.builder().build();
        }

        if (!ReadingAppConstants.ORDER_FULFILLED
                .equals(orderDetails.get().getOrderStatus())
                || ReadingAppConstants.ORDER_OUT_FOR_DELIVERY
                .equals(orderDetails.get().getOrderStatus())) {
            log.info("Order cannot be edited as it is {}", orderDetails.get().getOrderStatus());
        }

        log.info("Fetching book details with book Id: {}",
                orderDetails.get().getBookDetails().getId());
        BookDetails bookDetails = bookDetailsRepository
                .getById(orderDetails.get().getBookDetails().getId());

        int availableQuantities = bookDetails.getAvailableCopies();
        int revisedQuantity = (Integer.parseInt(editOrderRequest.getRevisedCopies())
                - orderDetails.get().getQuantityOrdered());
        if (availableQuantities < revisedQuantity) {
            log.info("Quantity requested not available");
            throw RequestedQuantityNotAvailable.builder().build();
        }
        orderDetails.get()
                .setQuantityOrdered(Integer
                        .parseInt(editOrderRequest.getRevisedCopies()));
        orderDetails.get().setOrderUpdatedOn(ReadingUtils.getCurrentTimestamp());
        orderDetailsRepository.save(orderDetails.get());
        log.info("Order with order Id: {} updated successfully",
                orderDetails.get().getId());

        log.info("Updating available copies for book Id: {} after order is edited",
                bookDetails.getId());
        bookDetails.setAvailableCopies(availableQuantities - revisedQuantity);
        bookDetails.setLastUpdatedOn(ReadingUtils.getCurrentTimestamp());
        bookDetailsRepository.save(bookDetails);
        log.info("Available copies count updated successfully");

        return EditOrderResponse.builder().orderId(String.valueOf(orderDetails.get().getId()))
                .revisedCopies(String.valueOf(orderDetails.get().getQuantityOrdered()))
                .status(ReadingAppConstants.ORDER_REVISED_SUCCESSFULLY)
                .build();
    }

    @Override
    public OrderDetailsResponse displayOrderDetailsById(String orderId) {
        log.info("Fetching order details with order Id: {}", orderId);
        Optional<OrderDetails> orderDetails = orderDetailsRepository
                .findById(Integer.valueOf(orderId));
        if (orderDetails.isEmpty()) {
            log.info("Order with id: {} was not found", orderId);
            throw OrderNotFoundException.builder().build();
        }
        log.info("Order with id: {} was found in DB", orderId);
        return OrderDetailsResponse.builder()
                .orderId(String.valueOf(orderDetails.get().getId()))
                .title(orderDetails.get().getBookDetails().getTitle())
                .author(orderDetails.get().getBookDetails().getAuthor())
                .customerName(orderDetails.get().getCustomerDetails().getFullName())
                .copiesOrdered(String.valueOf(orderDetails.get().getQuantityOrdered()))
                .orderPlacedOn(ReadingUtils
                        .getFormattedTimestamp(orderDetails.get().getOrderPlacedOn()))
                .build();
    }

    @Override
    public SuccessResponse orderDetailsByTimeInterval(
            OrderDetailsIntervalRequest orderDetailsInterval,
            int pageNumber, int recordCount) {

        PageRequest pageRequest = PageRequest.of(pageNumber, recordCount);

        log.info("Fetching order details for page: {} with record count: {}",
                pageNumber, recordCount);
        Page<OrderDetails> orderDetailsPages = orderDetailsRepository
                .findByOrderPlacedOnGreaterThanAndOrderPlacedOnLessThan(
                        Timestamp.valueOf(orderDetailsInterval.getStartTime()),
                        Timestamp.valueOf(orderDetailsInterval.getEndTime()), pageRequest);
        log.info("Order details for page: {} fetched successfully", pageNumber);

        List<OrderDetails> orderDetails = new ArrayList<>(orderDetailsPages.getContent());
        SuccessResponse<List<OrderDetailsResponse>> orderDetailsList = new SuccessResponse<>();
        if (orderDetails.size() != 0) {

            log.info("Total records fetched: {}", orderDetailsPages.getTotalElements());
            int totalNumberOfPages = (int) Math.ceil(orderDetailsPages.getTotalElements()
                    / Double.valueOf(recordCount));
            log.info("Total pages: {}", totalNumberOfPages);

            orderDetailsList.setTotalPageCount(totalNumberOfPages);

            List<OrderDetailsResponse> orderDetailsResponse = orderDetails
                    .stream().map(e -> OrderDetailsResponse.builder()
                            .orderId(String.valueOf(e.getId()))
                            .title(e.getBookDetails().getTitle())
                            .author(e.getBookDetails().getAuthor())
                            .customerName(e.getCustomerDetails().getFullName())
                            .copiesOrdered(String.valueOf(e.getQuantityOrdered()))
                            .orderPlacedOn(ReadingUtils
                                    .getFormattedTimestamp(e.getOrderPlacedOn()))
                            .build())
                    .collect(Collectors.toList());

            orderDetailsList.setData(orderDetailsResponse);
            return orderDetailsList;
        }
        log.info("Orders within interval: {} to {} were not found",
                orderDetailsInterval.getStartTime(), orderDetailsInterval.getEndTime());
        throw OrderNotFoundException.builder().build();
    }

    @Override
    public SuccessResponse getAllOrders(String customerId, int pageNumber, int recordCount) {

        log.info("Fetching customer details with customer Id: {}", customerId);
        Optional<CustomerDetails> customerDetails =
                customerDetailsRepository.findById(Integer.valueOf(customerId));

        if (customerDetails.isEmpty()) {
            log.info("Customer not found");
            throw CustomerNotFoundException.builder().build();
        }

        log.info("Fetching order details for page: {} with record count: {}",
                pageNumber, recordCount);
        PageRequest pageRequest = PageRequest.of(pageNumber, recordCount);
        Page<OrderDetails> orderDetailsPage =
                orderDetailsRepository.findByCustomerDetails(customerDetails.get(), pageRequest);
        log.info("Order details for page: {} fetched successfully", pageNumber);
        List<OrderDetails> orderDetails = new ArrayList<>(orderDetailsPage.getContent());
        SuccessResponse<List<OrderDetailsResponse>> orderDetailsSuccessResponse = new SuccessResponse<>();

        if (orderDetails.size() != 0) {
            log.info("Total records fetched: {}", orderDetailsPage.getTotalElements());
            int totalNumberOfPages = (int) Math.ceil(orderDetailsPage.getTotalElements()
                    / Double.valueOf(recordCount));
            log.info("Total pages: {}", totalNumberOfPages);
            orderDetailsSuccessResponse.setTotalPageCount(totalNumberOfPages);

            List<OrderDetailsResponse> orderDetailsResponseData = orderDetails
                    .stream().map(e -> OrderDetailsResponse.builder()
                            .orderId(String.valueOf(e.getId()))
                            .title(e.getBookDetails().getTitle())
                            .author(e.getBookDetails().getAuthor())
                            .customerName(e.getCustomerDetails().getFullName())
                            .copiesOrdered(String.valueOf(e.getQuantityOrdered()))
                            .orderPlacedOn(ReadingUtils
                                    .getFormattedTimestamp(e.getOrderPlacedOn()))
                            .build())
                    .collect(Collectors.toList());

            orderDetailsSuccessResponse.setData(orderDetailsResponseData);
            return orderDetailsSuccessResponse;
        }
        log.info("Orders within for the customer {} were not found", customerId);
        throw OrderNotFoundException.builder().build();
    }
}
