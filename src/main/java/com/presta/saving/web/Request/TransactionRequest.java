package com.presta.saving.web.Request;

import com.presta.saving.domain.Enums.PaymentMethod;
import com.presta.saving.domain.Enums.TransactionType;
import lombok.Data;

@Data
public class TransactionRequest {
    private double amount;
    private String memberNumber;
    private String accountNumber;
    private PaymentMethod paymentMethod;
    private TransactionType transactionType;
}
