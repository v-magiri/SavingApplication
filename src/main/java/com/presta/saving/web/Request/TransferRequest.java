package com.presta.saving.web.Request;

import com.presta.saving.domain.Enums.PaymentMethod;
import com.presta.saving.domain.Enums.TransactionType;
import lombok.Data;

@Data
public class TransferRequest {
    private double amount;
    private String memberNumber;
    private String transferAccountNumber;
    private String receiverAccountNumber;
    private PaymentMethod paymentMethod;
}
