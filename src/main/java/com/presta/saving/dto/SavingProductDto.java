package com.presta.saving.dto;

import java.time.LocalDateTime;

public interface SavingProductDto {
    String getProductName();

    double getInterestRate();
    String getProductId();
    String getDescription();
}
