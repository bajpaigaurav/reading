package getir.reading.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum BookServiceEnum {

    BOOK_NOT_FOUND("Requested book not found",
            HttpStatus.NOT_FOUND.toString()),
    BOOK_ALREADY_EXISTS("Book already exists in DB",
            HttpStatus.NOT_ACCEPTABLE.toString()),
    REQUESTED_QUANTITY_NOT_AVAILABLE("Requested quantity of books not available",
            HttpStatus.INTERNAL_SERVER_ERROR.toString());
    private String debugMessage;
    private String statusCode;

    BookServiceEnum(String debugMessage, String statusCode) {
        this.debugMessage = debugMessage;
        this.statusCode = statusCode;
    }
}
