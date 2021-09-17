package getir.reading.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
public class AppExceptionResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String debugMessage;
    private String statusCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<String> debugMessageList;

    AppExceptionResponse(String debugMessage, String statusCode) {
        this.debugMessage = debugMessage;
        this.statusCode = statusCode;
    }

    AppExceptionResponse(Set<String> debugMessageList, String statusCode) {
        this.debugMessageList = debugMessageList;
        this.statusCode = statusCode;
    }

}
