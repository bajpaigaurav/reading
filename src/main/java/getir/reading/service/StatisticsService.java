package getir.reading.service;

import getir.reading.model.CustomerMonthlyOrderStats;
import getir.reading.model.OrderAnalyticsResponse;
import org.springframework.stereotype.Component;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Component
public interface StatisticsService {

    OrderAnalyticsResponse getOrderAnalytics(int customerId, String month);
   List<CustomerMonthlyOrderStats> getOrderAnalyticsForCustomerId(int customerId);

}
