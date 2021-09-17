package getir.reading.Exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public class OrderNotRevisedException extends RuntimeException{
    @Builder.Default
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
}
