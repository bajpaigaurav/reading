package getir.reading.model;

import getir.reading.constants.ReadingAppConstants;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class RegisterCustomerRequest {
    @NotBlank(message = "First name is blank")
    private String firstName;
    @NotBlank(message = "Last name is blank")
    private String lastName;
    @NotBlank(message = "Username is blank")
    private String userName;
    @Email(message = "email is not valid")
    @NotBlank(message = "email is blank")
    private String email;
    @Pattern(regexp = ReadingAppConstants.REGEX_PHONE_NUMBER,
            message = "Phone number is not valid")
    private String phoneNumber;
    @NotBlank(message="Password should not be blank")
    @Size(min=8, max = 16, message = "Password should be larger than 8 characters and smaller than 16 characters")
    private String password;
}
