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
            if(customerAccount!=null){
                return "Customer registered successfully";
            }
            else{
                return "Customer registration did not complete";
            }
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

    public List<CustomerDto> getAllCustomers(int pageSize,int page){
//        Pageable pageable= PageRequest.of(page,pageSize);
        return customerRepository.findAllByOrderByCreatedAtDesc();
    }
//    public String updateCustomerDetails(Long customerId){
//
//    }


}
