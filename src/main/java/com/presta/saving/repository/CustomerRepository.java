package com.presta.saving.repository;

import com.presta.saving.domain.Customer;
import com.presta.saving.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Long> {
    boolean existsByIdNumber(String username);

    List<CustomerDto> findAllByOrderByCreatedAtDesc();

    CustomerDto findCustomerByMemberNumber(String memberNumber);

    CustomerDto findCustomerByIdNumber(String idNumber);

    CustomerDto findCustomerById(long id);

    Customer getCustomerByMemberNumber(String memberNumber);


}
