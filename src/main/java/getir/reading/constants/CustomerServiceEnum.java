package getir.reading.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CustomerServiceEnum {

    CUSTOMER_NOT_FOUND("Customer not found", HttpStatus.NOT_FOUND.toString()),
    CUSTOMER_ALREADY_EXISTS("Customer with this email/phone number already exists", HttpStatus.NOT_ACCEPTABLE.toString());

    private String debugMessage;
    private String statusCode;

    CustomerServiceEnum(String debugMessage, String statusCode) {
        this.debugMessage = debugMessage;
        this.statusCode = statusCode;
    }
}
