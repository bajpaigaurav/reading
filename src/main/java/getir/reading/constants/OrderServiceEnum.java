package getir.reading.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum OrderServiceEnum {

    ORDER_NOT_FOUND("Order does not exit",
            HttpStatus.NOT_FOUND.toString()),
    TIME_ENTERED_INCORRECT("Time entered format must be " +
            "yyyy-MM-dd HH:mm:ss and start time must be before end time",
            HttpStatus.BAD_REQUEST.toString());

    private String debugMessage;
    private String statusCode;

    OrderServiceEnum(String debugMessage, String statusCode) {
        this.debugMessage = debugMessage;
        this.statusCode = statusCode;
    }
}

