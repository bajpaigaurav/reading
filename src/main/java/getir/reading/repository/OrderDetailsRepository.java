package getir.reading.repository;

import getir.reading.dao.CustomerDetails;
import getir.reading.dao.OrderDetails;
import getir.reading.model.CustomerMonthlyOrderStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    Page<OrderDetails>
    findByOrderPlacedOnGreaterThanAndOrderPlacedOnLessThan(
            @Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime, Pageable page);

    Page<OrderDetails> findByCustomerDetails(
            @Param("customerDetails") CustomerDetails customerDetails, Pageable pageRequest);

    List<OrderDetails> findByOrderPlacedOnBetween(@Param("startTimestamp") Timestamp startTimestamp,
                                                  @Param("endTimestamp") Timestamp endTimestamp);
    @Query("SELECT new getir.reading.model." +
            "CustomerMonthlyOrderStats(o.month, COUNT(o.month),SUM(o.quantityOrdered),SUM(o.totalAmount), o.customerDetails.id)" +
            " FROM OrderDetails AS o GROUP BY o.month")
    List<CustomerMonthlyOrderStats> aggregateOrdersBasedOnCustomerId(@Param("customerDetails") CustomerDetails customerDetails);
}
