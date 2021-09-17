package getir.reading.model;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditedCustomerResponse {
    private int id;
    private String userName;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;
    private Timestamp lastUpdatedOn;
}
