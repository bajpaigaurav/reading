package getir.reading.Exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public class CustomerNotFoundException extends RuntimeException {
    @Builder.Default
    private HttpStatus httpStatus = HttpStatus.NOT_FOUND;

}