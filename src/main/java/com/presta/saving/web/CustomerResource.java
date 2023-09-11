package com.presta.saving.web;

import com.presta.saving.domain.Customer;
import com.presta.saving.dto.CustomerDto;
import com.presta.saving.dto.RestResponse;
import com.presta.saving.repository.CustomerRepository;
import com.presta.saving.services.CustomerService;
import com.presta.saving.web.Request.CustomerRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @GetMapping
    public ResponseEntity<?> getCustomers(){
        try{
            List<CustomerDto> customerDtoList=customerRepository.findAllByOrderByCreatedAtDesc();
            return new ResponseEntity<>(customerDtoList,HttpStatus.OK);
        }catch (Exception e){
            String message=String.format("Error occurred while fetching customers");
            log.error(e.getMessage());
            return new ResponseEntity<>(new RestResponse(true, message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerRequest customerRequest,
                                            @PathVariable Long id){
        try{
            Optional<Customer> customer=customerRepository.findById(id);
            if(customer.isPresent()){
                Customer updatedCustomer=customer.get();
                String updatedMessage=customerService.updateCustomerDetails(id,customerRequest);
                return new ResponseEntity<>(new RestResponse(false,updatedMessage),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new RestResponse(true,"Customer does not Exist"),HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            String message=String.format("Error occurred while updating customer identified by ID: ",id);
            log.error(e.getMessage());
            return new ResponseEntity<>(new RestResponse(true, message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id){
        try{
            CustomerDto customerDto=customerRepository.findCustomerById(id);
            return new ResponseEntity<>(customerDto,HttpStatus.OK);
        }catch (Exception e){
            String message=String.format("Error occurred while updating customer identified by ID: ",id);
            log.error(e.getMessage());
            return new ResponseEntity<>(new RestResponse(true, message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "filter/memberNumber")
    public ResponseEntity<?> getCustomerByMemberNumber(@RequestParam("memberNumber") String memberNumber){
        try{
            CustomerDto customerDto=customerService.getCustomerByMemberNumber(memberNumber);
            return new ResponseEntity<>(customerDto,HttpStatus.OK);
        }catch (Exception e){
            String message=String.format("Error occurred while updating customer identified by memberNumber: ",memberNumber);
            log.error(e.getMessage());
            return new ResponseEntity<>(new RestResponse(true, message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
