package getir.reading.model;

import getir.reading.constants.ReadingAppConstants;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class EditOrderRequest {

    @Pattern(regexp = ReadingAppConstants.REGEX_ONLY_NUMBER,
            message = "Order Id incorrect")
    private String orderId;
    @Pattern(regexp = ReadingAppConstants.REGEX_ONLY_NUMBER,
            message = "Copies entered incorrect")
    private String revisedCopies;
}
