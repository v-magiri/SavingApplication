package com.presta.saving.repository;

import com.presta.saving.domain.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface TransactionRepository extends CrudRepository<Transaction,Long> {

}
