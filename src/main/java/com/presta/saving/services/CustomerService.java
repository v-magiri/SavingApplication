package com.presta.saving.services;

import com.presta.saving.domain.Account;
import com.presta.saving.domain.Customer;
import com.presta.saving.domain.SavingProduct;
import com.presta.saving.dto.CustomerDto;
import com.presta.saving.repository.AccountRepository;
import com.presta.saving.repository.CustomerRepository;
import com.presta.saving.repository.SavingProductRepository;
import com.presta.saving.web.Request.CustomerRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    private final SavingProductRepository savingProductRepository;

    private final AccountRepository accountRepository;

    public CustomerService(CustomerRepository customerRepository,
                           SavingProductRepository savingProductRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.savingProductRepository = savingProductRepository;
        this.accountRepository = accountRepository;
    }

    public String registerCustomer(CustomerRequest customerRequest){
        try{
            Customer customer=new Customer();
            customer.setFirstName(customerRequest.getFirstName());
            customer.setLastName(customerRequest.getLastName());
            customer.setMiddleName(customerRequest.getMiddleName());
            customer.setAddress(customerRequest.getAddress());
            customer.setCreatedAt(LocalDateTime.now());
            customer.setCreatedBy("SYSTEM");
            customer.setIdNumber(customerRequest.getIdNumber());
            customer.setEmailAddress(customerRequest.getEmailAddress());
            customer.setPhoneNumber(customerRequest.getPhoneNumber());

            int randNo = (int) ((Math.random() * (99999 - 1)) + 1);
            String  customerL= String.format("%05d", randNo);
            String  memberNumber="101"+customerL+customerRequest.getPhoneNumber().substring(5,9);

            customer.setMemberNumber(memberNumber);

            Customer newCustomer= customerRepository.save(customer);
            Account customerAccount=createSavingAccount(customerRequest,newCustomer);
            return "Customer registered successfully";
        }catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    private Account createSavingAccount(CustomerRequest customerRequest,Customer customer) {
        try{
            Account newAccount= new Account();
            newAccount.setCreatedAt(LocalDateTime.now());
            newAccount.setCustomer(customer);
            SavingProduct product=savingProductRepository.findByProductName(customerRequest.getAccountType());
            String accountNumber=generateAccountNumber();
            newAccount.setAccountNumber(accountNumber);
            newAccount.setSavingsProduct(product);
            newAccount.setBalance(customerRequest.getDepositAmount());
            newAccount.setCreatedBy("SYSTEM");
            Account customerAccount=accountRepository.save(newAccount);
            List<Account> savingAccount=new ArrayList<>();
            savingAccount.add(customerAccount);
            customer.setSavingsAccounts(savingAccount);
            customerRepository.save(customer);
            return customerAccount;
        }catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
    private String generateAccountNumber(){
        Random random=new Random();
        StringBuilder accountNumber=new StringBuilder(14);

        for(int i=0;i<14;i++){
            int randomDigit=random.nextInt(10);
            accountNumber.append(randomDigit);
        }
        return accountNumber.toString();
    }


    public List<CustomerDto> getAllCustomers(){
        return customerRepository.findAllByOrderByCreatedAtDesc();
    }
    public String updateCustomerDetails(Long customerId
            ,CustomerRequest customerRequest){
        try{
            log.info("Updating Customer Details");
            Optional<Customer> optionalCustomer=customerRepository.findById(customerId);
            if(optionalCustomer.isPresent()){
                Customer customer=optionalCustomer.get();

                if(customerRequest.getFirstName() != null &&
                        customerRequest.getFirstName().length()> 0
                && !customerRequest.getFirstName().equals(customer.getFirstName())){
                    customer.setFirstName(customerRequest.getFirstName());
                }

                if(customerRequest.getLastName() != null &&
                        customerRequest.getLastName().length()> 0
                        && !customerRequest.getLastName().equals(customer.getLastName())){
                    customer.setLastName(customerRequest.getLastName());
                }

                if(customerRequest.getMiddleName() != null &&
                        customerRequest.getMiddleName().length()> 0
                        && !customerRequest.getMiddleName().equals(customer.getMiddleName())){
                    customer.setMiddleName(customerRequest.getMiddleName());
                }

                if(customerRequest.getMiddleName() != null &&
                        customerRequest.getMiddleName().length()> 0
                        && !customerRequest.getMiddleName().equals(customer.getMiddleName())){
                    customer.setMiddleName(customerRequest.getMiddleName());
                }

                if(customerRequest.getIdNumber() != null &&
                        customerRequest.getIdNumber().length()> 0
                        && !customerRequest.getIdNumber().equals(customer.getIdNumber())){
                    customer.setIdNumber(customerRequest.getIdNumber());
                }

                if(customerRequest.getEmailAddress() != null &&
                        customerRequest.getEmailAddress().length()> 0
                        && !customerRequest.getEmailAddress().equals(customer.getEmailAddress())){
                    customer.setEmailAddress(customerRequest.getEmailAddress());
                }

                if(customerRequest.getAddress() != null &&
                        customerRequest.getAddress().length()> 0
                        && !customerRequest.getAddress().equals(customer.getAddress())){
                    customer.setAddress(customerRequest.getAddress());
                }

                if(customerRequest.getPhoneNumber() != null &&
                        customerRequest.getPhoneNumber().length()> 0
                        && !customerRequest.getPhoneNumber().equals(customer.getPhoneNumber())){
                    customer.setPhoneNumber(customerRequest.getPhoneNumber());
                }

                customer.setUpdateAt(LocalDateTime.now());
                customer.setUpdatedBy("SYSTEM");

                customerRepository.save(customer);
                return "Updated Customer Details";
            }else{
                throw new IllegalStateException("Customer does not exist");
            }
        }catch (Exception e){
            throw new IllegalStateException("Failed to updated customer details");
        }
    }

    public CustomerDto getCustomerByMemberNumber(String memberNumber){
        try{
            return customerRepository.findCustomerByMemberNumber(memberNumber);
        }catch(Exception e){
            throw new IllegalStateException("Failed to get customer with memberNumber:"+memberNumber);
        }
    }

    public String deleteCustomer(String memberNumber){
        try{
            Customer customer=customerRepository.getCustomerByMemberNumber(memberNumber);
            customerRepository.delete(customer);
            return "Deleted Customer Successfully";
        }catch(Exception e){
            throw new IllegalStateException("Failed to delete customer");
        }
    }

    //open additional saving product account for customer
//    public String openAdditionalAccount(){
//        try{
//
//        }catch(Exception e){
//            throw new IllegalStateException("Failed to open  customer additional Account");
//        }
//    }



}
