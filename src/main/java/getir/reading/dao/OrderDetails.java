package getir.reading.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_details")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int quantityOrdered;
    private String orderStatus;
    private Timestamp orderPlacedOn;
    private Timestamp orderUpdatedOn;
    private double totalAmount;
    private String month;
    @ManyToOne
    @JoinColumn(name = "book_details")
    private BookDetails bookDetails;
    @ManyToOne
    @JoinColumn(name = "customer_details")
    private CustomerDetails customerDetails;
}
