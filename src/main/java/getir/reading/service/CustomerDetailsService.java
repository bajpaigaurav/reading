package getir.reading.service;

import getir.reading.dao.CustomerDetails;
import getir.reading.model.*;
import org.springframework.stereotype.Component;

@Component
public interface CustomerDetailsService {

    CustomerDetails displayCustomerDetails(String customerId);

    String registerNewCustomer(RegisterCustomerRequest registerCustomerRequest);

    EditedCustomerResponse editCustomerDetails(EditCustomerDetailsRequest customerDetails);

    DeleteCustomerResponse deleteCustomer(String customerId);

}
