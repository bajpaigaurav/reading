package getir.reading.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<T> {
    private Integer totalPageCount;
    private T data;

}
