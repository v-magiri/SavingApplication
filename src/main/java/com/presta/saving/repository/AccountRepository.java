package com.presta.saving.repository;

import com.presta.saving.domain.Account;
import com.presta.saving.dto.AccountDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account,Long> {
    AccountDto getAccountByAccountNumber(String accountNumber);

    Account findAccountByAccountNumber(String accountNumber);

}
