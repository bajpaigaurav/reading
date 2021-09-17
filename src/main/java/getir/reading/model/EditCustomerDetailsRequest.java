package getir.reading.model;

import getir.reading.constants.ReadingAppConstants;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class EditCustomerDetailsRequest {
    @Pattern(regexp = ReadingAppConstants.REGEX_ONLY_NUMBER,
    message = "id is not valid")
    private String id;
    @NotBlank (message = "First name is blank")
    private String firstName;
    @NotBlank (message = "Last name is blank")
    private String lastName;
    @Email(message = "email is not valid")
    private String email;
    @Pattern(regexp = ReadingAppConstants.REGEX_PHONE_NUMBER, message = "Phone number is not valid")
    private String phoneNumber;
}
