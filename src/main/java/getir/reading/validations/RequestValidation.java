package getir.reading.validations;

import getir.reading.Exception.TimeFormatInvalidException;
import getir.reading.model.OrderDetailsIntervalRequest;
import getir.reading.util.ReadingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;

@Component
@Slf4j
public class RequestValidation {

    public void validateOrderDetailsWithTimeRequest(OrderDetailsIntervalRequest orderDetailsIntervalRequest) {
        Timestamp startTime;
        Timestamp endTime;
        try {
            startTime = ReadingUtils.convertStringToTimestamp(orderDetailsIntervalRequest.getStartTime());
            endTime = ReadingUtils.convertStringToTimestamp(orderDetailsIntervalRequest.getEndTime());
        } catch (ParseException ex) {
            log.error("Date format entered invalid");
            throw TimeFormatInvalidException.builder().build();
        }
        if (startTime.after(endTime)) {
            log.info("Start date-time entered comes after end date-time");
            throw TimeFormatInvalidException.builder().build();
        }
        log.info("Time format from request verified");
    }
}
