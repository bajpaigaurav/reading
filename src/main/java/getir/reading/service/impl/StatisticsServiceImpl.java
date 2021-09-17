package getir.reading.service.impl;

import getir.reading.Exception.CustomerNotFoundException;
import getir.reading.Exception.TimeFormatInvalidException;
import getir.reading.constants.MonthEnum;
import getir.reading.dao.CustomerDetails;
import getir.reading.dao.OrderDetails;
import getir.reading.model.CustomerMonthlyOrderStats;
import getir.reading.model.OrderAnalyticsResponse;
import getir.reading.repository.CustomerDetailsRepository;
import getir.reading.repository.OrderDetailsRepository;
import getir.reading.service.StatisticsService;
import getir.reading.util.ReadingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    CustomerDetailsRepository customerDetailsRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Override
    public OrderAnalyticsResponse getOrderAnalytics(int customerId, String month) {
        log.info("Fetching details for customer Id: {}", customerId);
        Optional<CustomerDetails> customerDetails = customerDetailsRepository.findById(customerId);
        if (customerDetails.isEmpty()) {
            log.info("Customer details with id: {} not found", customerId);
            throw CustomerNotFoundException.builder().build();
        }

        int monthValue = MonthEnum.valueOf(
                month.toUpperCase(Locale.ROOT)).getMonth();
        Timestamp startTimestamp;
        Timestamp endTimestamp;
        try {
            log.info("Fetching start and end dates for month: {}", month);
            startTimestamp = ReadingUtils.startTimestamp(monthValue);
            endTimestamp = ReadingUtils.endTimestamp(monthValue);
        } catch (ParseException ex) {
            log.error("Month entered invalid");
            throw TimeFormatInvalidException.builder().build();
        }
        log.info("Start timestamp: {}", startTimestamp);
        log.info("End timestamp: {}", endTimestamp);
        log.info("Fetching orders for time interval: {} through {} for customer Id: {}",
                startTimestamp, endTimestamp, customerId);

        List<OrderDetails> orderDetailsInInterval = orderDetailsRepository
                .findByOrderPlacedOnBetween(startTimestamp, endTimestamp);

        log.info("Order details fetched successfully");

        int totalBookCount = orderDetailsInInterval.stream()
                .mapToInt(e -> e.getQuantityOrdered())
                .sum();

        log.info("Total books ordered: {}", totalBookCount);

        double totalPurchasedAmount = orderDetailsInInterval.stream()
                .mapToDouble(e -> e.getQuantityOrdered()
                        * e.getBookDetails().getPrice())
                .sum();

        log.info("Total purchase amount: {}", totalPurchasedAmount);

        OrderAnalyticsResponse analyticsResponse = OrderAnalyticsResponse.builder()
                .month(month)
                .customerId(String.valueOf(customerId))
                .totalOrderCount(String.valueOf(orderDetailsInInterval.size()))
                .totalBookCount(String.valueOf(totalBookCount))
                .totalPurchasedAmount(String.valueOf(totalPurchasedAmount))
                .build();

        return analyticsResponse;
    }

    @Override
    public List<CustomerMonthlyOrderStats> getOrderAnalyticsForCustomerId(int customerId) {
        log.info("Fetching details for customer Id: {}", customerId);
        Optional<CustomerDetails> customerDetails = customerDetailsRepository.findById(customerId);
        if (customerDetails.isEmpty()) {
            log.info("Customer details with id: {} not found", customerId);
            throw CustomerNotFoundException.builder().build();
        }

        List<CustomerMonthlyOrderStats> customerMonthlyOrderStatsList = orderDetailsRepository
                .aggregateOrdersBasedOnCustomerId(customerDetails.get());


        log.info("Order details fetched successfully");



        return customerMonthlyOrderStatsList;
    }
}
