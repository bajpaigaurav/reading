package getir.reading.service.impl;

import getir.reading.Exception.CustomerAlreadyExistsException;
import getir.reading.Exception.CustomerNotFoundException;
import getir.reading.constants.ReadingAppConstants;
import getir.reading.dao.CustomerDetails;
import getir.reading.model.*;
import getir.reading.repository.CustomerDetailsRepository;
import getir.reading.service.CustomerDetailsService;
import getir.reading.util.ReadingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomerDetailsServiceImpl implements CustomerDetailsService {
    @Autowired
    CustomerDetailsRepository customerDetailsRepository;

    @Override
    public CustomerDetails displayCustomerDetails(String customerId) {
        log.info("Fetching customer details with customer Id: {} from DB", customerId);
        Optional<CustomerDetails> customerDetails = customerDetailsRepository.
                findById(Integer.parseInt(customerId));
        if (customerDetails.isPresent()) {
            log.info("Customer details found for customer Id: {}", customerId);
            return customerDetails.get();
        }

        log.info("Customer details not found for customer Id: {}", customerId);
        throw CustomerNotFoundException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    @Override
    public String registerNewCustomer(RegisterCustomerRequest registerCustomerRequest) {
        log.info("Finding details for customer phone: {} and email: {}",
                registerCustomerRequest.getPhoneNumber(), registerCustomerRequest.getEmail());
        Optional<CustomerDetails> customerDetails = customerDetailsRepository
                .findByEmailOrPhoneNumber(registerCustomerRequest.getEmail(),
                        registerCustomerRequest.getPhoneNumber());
        if (customerDetails.isEmpty()) {
            log.info("Customer does not exist with phone: {} and email: {}",
                    registerCustomerRequest.getPhoneNumber(), registerCustomerRequest.getEmail());
            CustomerDetails newCustomerDetails = CustomerDetails.builder()
                    .userName(registerCustomerRequest.getUserName())
                    .email(registerCustomerRequest.getEmail())
                    .phoneNumber(registerCustomerRequest.getPhoneNumber())
                    .lastUpdatedOn(ReadingUtils.getCurrentTimestamp())
                    .password(registerCustomerRequest.getPassword())
                    .lastName(registerCustomerRequest.getLastName())
                    .firstName(registerCustomerRequest.getFirstName())
                    .build();
            customerDetailsRepository.save(newCustomerDetails);
            log.info("New customer saved in DB with Id: {}", newCustomerDetails.getId());
            return String.valueOf(newCustomerDetails.getId());
        }
        log.info("Customer with Id: {} already exists with phone: {} and email: {}",
                customerDetails.get().getId(), registerCustomerRequest.getPhoneNumber(),
                registerCustomerRequest.getEmail());
        throw CustomerAlreadyExistsException.builder()
                .httpStatus(HttpStatus.NOT_ACCEPTABLE)
                .build();
    }

    @Override
    public EditedCustomerResponse editCustomerDetails(EditCustomerDetailsRequest
                                                       customerDetailsRequest) {

        log.info("Fetching customer details with customer Id: {} from DB",
                customerDetailsRequest.getId());
        Optional<CustomerDetails> customerDetails = customerDetailsRepository
                .findById(Integer.valueOf
                        (customerDetailsRequest.getId()));
        // duplicate details override check
        Optional<CustomerDetails> duplicateCustomerDetails = customerDetailsRepository
                .findByEmailOrPhoneNumber(customerDetailsRequest.getEmail(),customerDetailsRequest.getPhoneNumber());
        if (customerDetails.isPresent() && !duplicateCustomerDetails.isPresent()) {
            log.info("Customer with Id: {} found in DB",
                    customerDetailsRequest.getId());
            customerDetails.get().setFirstName(customerDetailsRequest.getFirstName());
            customerDetails.get().setLastName(customerDetailsRequest.getLastName());
            customerDetails.get().setEmail(customerDetailsRequest.getEmail());
            customerDetails.get().setPhoneNumber(customerDetailsRequest.getPhoneNumber());
            customerDetails.get().setLastUpdatedOn(ReadingUtils.getCurrentTimestamp());
            // sending back saved details
            CustomerDetails save = customerDetailsRepository.save(customerDetails.get());
            log.info("Customer details with Id: {} updated successfully",
                    customerDetails.get().getId());
            return EditedCustomerResponse.builder()
                    .id(save.getId())
                    .firstName(save.getFirstName())
                    .lastName(save.getLastName())
                    .userName(save.getUserName())
                    .lastUpdatedOn(save.getLastUpdatedOn())
                    .phoneNumber(save.getPhoneNumber())
                    .email(save.getEmail())
                    .build();

        }
        log.info("Customer with Id: {} not found in DB",
                customerDetailsRequest.getId());
        throw CustomerNotFoundException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    @Override
    public DeleteCustomerResponse deleteCustomer(String customerId) {
        log.info("Fetching customer details with customer Id: {} from DB", customerId);
        Optional<CustomerDetails> customerDetails = customerDetailsRepository
                .findById(Integer.valueOf(customerId));
        if (customerDetails.isPresent()) {
            log.info("Customer with Id: {} not found in DB", customerId);
            DeleteCustomerResponse deleteCustomerResponse = DeleteCustomerResponse.builder()
                    .customerId(String.valueOf(customerDetails.get().getId()))
                    .customerName(customerDetails.get().getUserName())
                    .status(ReadingAppConstants.CUSTOMER_DE_REGISTERED)
                    .build();
            customerDetailsRepository.delete(customerDetails.get());
            log.info("Customer details with Id: {} deleted successfully", customerId);
            return deleteCustomerResponse;
        }
        log.info("Customer with Id: {} not found in DB", customerId);
        throw CustomerNotFoundException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }
}
