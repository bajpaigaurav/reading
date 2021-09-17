package getir.reading.controller;

import getir.reading.model.CustomerMonthlyOrderStats;
import getir.reading.model.OrderAnalyticsResponse;
import getir.reading.model.SuccessResponse;
import getir.reading.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@Slf4j
@RequestMapping("/getir/analytics")
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @GetMapping("/count/orders/monthly")
    public ResponseEntity<SuccessResponse> getOrderStatsForAMonth(@RequestParam("customerId")
                                                                @NotBlank @NotNull String customerId,
                                                                @RequestParam("month")
                                                                @NotBlank @NotNull String month) {
        log.info("In controller for statistics for customer Id: {} for the month of: {}",
                customerId, month);
        OrderAnalyticsResponse analyticsResponse = statisticsService
                .getOrderAnalytics(Integer.parseInt(customerId), month);
        SuccessResponse<OrderAnalyticsResponse> successResponse = new SuccessResponse<>();
        successResponse.setData(analyticsResponse);
        return new ResponseEntity<>(successResponse, HttpStatus.FOUND);
    }

    @GetMapping("/count/orders/catalogue")
    public ResponseEntity<SuccessResponse> getMonthlyOrderStatsForACustomer(@RequestParam("customerId")
                                                                @NotBlank @NotNull String customerId
                                                                ) {
        log.info("In controller for statistics for customer Id: {} for the month of: {}",
                customerId);
        List<CustomerMonthlyOrderStats> analyticsResponse = statisticsService
                .getOrderAnalyticsForCustomerId(Integer.parseInt(customerId));
        SuccessResponse<List<CustomerMonthlyOrderStats>> successResponse = new SuccessResponse<>();
        successResponse.setData(analyticsResponse);
        return new ResponseEntity<>(successResponse, HttpStatus.FOUND);
    }
}
