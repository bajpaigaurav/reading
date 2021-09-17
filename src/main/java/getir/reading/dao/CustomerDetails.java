package getir.reading.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_details")
public class CustomerDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String userName;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;
    private String password;
    private Timestamp lastUpdatedOn;

    public String getFullName() {
        return firstName+ " "+ lastName;
    }

}
