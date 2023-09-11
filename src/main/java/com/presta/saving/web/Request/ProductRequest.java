package com.presta.saving.web.Request;

import lombok.Data;

@Data
public class ProductRequest {
    private String productName;
    private double interestRate;
    private String description;
    private double minimumOperatingBalance;

}
