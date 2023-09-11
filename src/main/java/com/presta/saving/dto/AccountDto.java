package com.presta.saving.dto;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountDto {
    String getAccountDto();

    double getBalance();
    List<TransactionView> getTransactions();

    ProductView getSavingsProduct();

    CustomerView getCustomerView();

    interface CustomerView{
        String getMemberNumber();
    }
    interface ProductView{
        String getProductName();
        double getInterestRate();
        double getMinimumOperatingBalance();
    }
    interface TransactionView{
        String getTransactionNumber();
        LocalDateTime getTransactedAt();
        double getAmount();
    }
}
