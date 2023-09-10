package com.presta.saving.web;

import com.presta.saving.dto.RestResponse;
import com.presta.saving.repository.CustomerRepository;
import com.presta.saving.services.CustomerService;
import com.presta.saving.web.Request.CustomerRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/customers")
public class CustomerResource {
    private final static String CUSTOMER_ALREADY_EXISTS="Customer with ID Number %s already exists";
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    public CustomerResource(CustomerRepository customerRepository,
                            CustomerService customerService) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerRequest customerRequest){
        log.info("Creating User");
        try{
            if(customerRepository.existsByIdNumber(customerRequest.getIdNumber())){
                String message=String.format(CUSTOMER_ALREADY_EXISTS,customerRequest.getIdNumber());
                return new ResponseEntity<>(new RestResponse(false,message),HttpStatus.BAD_REQUEST);
            }else{
                String message=customerService.registerCustomer(customerRequest);
                return new ResponseEntity<>(new RestResponse(false,message),HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(new RestResponse(true,e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    public ResponseEntity<?> getCustomers(){
//
//    }
//
//    public ResponseEntity<?> updateCustomer(@RequestBody CustomerRequest customerRequest){
//
//    }
//
//    public ResponseEntity<?> getCustomerById(){
//
//    }
//
//
//    public ResponseEntity<?> deleteProduct(){
//
//    }




}
