package getir.reading.Exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public class TimeFormatInvalidException extends RuntimeException {
    @Builder.Default
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

}
