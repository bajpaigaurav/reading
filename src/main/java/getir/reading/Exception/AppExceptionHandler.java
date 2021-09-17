package getir.reading.Exception;

import getir.reading.constants.BookServiceEnum;
import getir.reading.constants.CustomerServiceEnum;
import getir.reading.constants.OrderServiceEnum;
import getir.reading.model.AppExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class AppExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class,
            MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppExceptionResponse handleBadRequest(Exception ex) {
        List<ObjectError> errorList = ((MethodArgumentNotValidException) ex)
                .getBindingResult().getAllErrors();
        HashSet<String> errorMessages = (HashSet<String>) errorList.stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.toSet());

        return AppExceptionResponse.builder()
                .debugMessageList(errorMessages.size() > 1
                        ? errorMessages
                        : null)
                .debugMessage(errorMessages.size() == 1
                        ? String.valueOf(errorMessages)
                        : null)
                .statusCode(HttpStatus.BAD_REQUEST.toString())
                .build();
    }

    @ExceptionHandler(BooksNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppExceptionResponse handleBooksNotFoundException(BooksNotFoundException ex) {
        return AppExceptionResponse.builder()
                .debugMessage(BookServiceEnum.BOOK_NOT_FOUND.getDebugMessage())
                .statusCode(HttpStatus.NOT_FOUND.toString())
                .build();
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppExceptionResponse handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return AppExceptionResponse.builder()
                .debugMessage(CustomerServiceEnum.CUSTOMER_NOT_FOUND.getDebugMessage())
                .statusCode(HttpStatus.NOT_FOUND.toString())
                .build();
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public AppExceptionResponse handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex) {
        return AppExceptionResponse.builder()
                .debugMessage(CustomerServiceEnum.CUSTOMER_ALREADY_EXISTS.getDebugMessage())
                .statusCode(HttpStatus.NOT_ACCEPTABLE.toString())
                .build();
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public AppExceptionResponse handleBookAlreadyExistsException(BookAlreadyExistsException ex) {
        return AppExceptionResponse.builder()
                .debugMessage(BookServiceEnum.BOOK_ALREADY_EXISTS.getDebugMessage())
                .statusCode(HttpStatus.NOT_ACCEPTABLE.toString())
                .build();
    }

    @ExceptionHandler(RequestedQuantityNotAvailable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppExceptionResponse handleRequestedQuantityNotAvailable(RequestedQuantityNotAvailable ex) {
        return AppExceptionResponse.builder()
                .debugMessage(BookServiceEnum.REQUESTED_QUANTITY_NOT_AVAILABLE.getDebugMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .build();
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppExceptionResponse handleOrderNotFoundException(OrderNotFoundException ex) {
        return AppExceptionResponse.builder()
                .debugMessage(OrderServiceEnum.ORDER_NOT_FOUND.getDebugMessage())
                .statusCode(HttpStatus.NOT_FOUND.toString())
                .build();
    }

    @ExceptionHandler(TimeFormatInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppExceptionResponse handleTimeFormatInvalidException(TimeFormatInvalidException ex) {
        return AppExceptionResponse.builder()
                .debugMessage(OrderServiceEnum.TIME_ENTERED_INCORRECT.getDebugMessage())
                .statusCode(HttpStatus.BAD_REQUEST.toString())
                .build();
    }


}
