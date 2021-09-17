package getir.reading.controller;

import getir.reading.constants.ReadingAppConstants;
import getir.reading.dao.CustomerDetails;
import getir.reading.model.*;
import getir.reading.service.CustomerDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Validated
@RestController
@Slf4j
@RequestMapping("/getir/reading/customer")
public class CustomerController {

    @Autowired
    CustomerDetailsService customerDetailsService;

    @GetMapping("/details")
    public ResponseEntity<SuccessResponse> customerDetails(@RequestParam("customerId") @NotBlank String customerId) {
        log.info("In customer controller to find details of customer Id: {}", customerId);
        CustomerDetails details = customerDetailsService.displayCustomerDetails(customerId);
        SuccessResponse<CustomerDetails> successResponse = new SuccessResponse<>();
        successResponse.setData(details);
        return new ResponseEntity<>(successResponse, HttpStatus.FOUND);
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> register(@Valid @RequestBody RegisterCustomerRequest customerDetailsRequest) {
        log.info("In customer controller to register customer details");
        String customerId = customerDetailsService.registerNewCustomer(customerDetailsRequest);
        RegisterCustomerResponse registerCustomerResponse = RegisterCustomerResponse.builder()
                .customerId(customerId)
                .status(ReadingAppConstants.CUSTOMER_REGISTERED)
                .build();
        SuccessResponse<RegisterCustomerResponse> successResponse = new SuccessResponse<>();
        successResponse.setData(registerCustomerResponse);
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
    }

    @PutMapping("details/edit")
    public ResponseEntity<SuccessResponse> editCustomerDetails(@Valid @RequestBody EditCustomerDetailsRequest
                                                                       editCustomerDetails) {
        log.info("In customer controller to edit customer details with customer Id: {}",
                editCustomerDetails.getId());
        EditedCustomerResponse editedCustomerResponse = customerDetailsService.editCustomerDetails(editCustomerDetails);
        SuccessResponse<EditedCustomerResponse> successResponse = new SuccessResponse<>();
        successResponse.setData(editedCustomerResponse);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteCustomerResponse> deleteCustomer(@RequestParam @NotBlank
                                                                 @Pattern(regexp = ReadingAppConstants.REGEX_ONLY_NUMBER)
                                                                         String customerId) {
        log.info("In customer controller to edit customer details with customer Id: {}", customerId);
        DeleteCustomerResponse deleteCustomerResponse = customerDetailsService.deleteCustomer(customerId);
        return new ResponseEntity<>(deleteCustomerResponse, HttpStatus.OK);
    }
}