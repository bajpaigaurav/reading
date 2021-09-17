package getir.reading.model;

import getir.reading.constants.ReadingAppConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class SaveBookRequest {

    @NotBlank(message = "Title is not valid")
    private String title;
    @NotBlank(message = "Author name is not valid")
    private String author;
    @Pattern(regexp = ReadingAppConstants.REGEX_ONLY_NUMBER,
            message = "isbn length must numbers only")
    private String isbn;
    @Pattern(regexp = ReadingAppConstants.REGEX_ONLY_NUMBER,
            message = "Available copies value is not valid")
    private String availableCopies;
    @Pattern(regexp = ReadingAppConstants.REGEX_PRICE,
            message = "Price is not valid")
    @NotBlank(message = "Price is blank")
    @NotNull(message = "Price is null")
    private String price;
}
