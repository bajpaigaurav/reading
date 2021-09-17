package getir.reading.model;

import getir.reading.constants.ReadingAppConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CreateOrderRequest {
    @NotBlank (message = "Title is blank")
    private String title;
    @NotBlank(message = "Author name is blank")
    private String author;
    @Pattern(regexp = ReadingAppConstants.REGEX_ONLY_NUMBER,
            message = "Copies entered incorrect")
    private String copiesOrdered;
    @NotBlank(message = "Customer id is blank")
    private String customerId;

}
