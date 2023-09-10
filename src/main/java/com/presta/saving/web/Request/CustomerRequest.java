package com.presta.saving.web.Request;

import lombok.Data;

@Data
public class CustomerRequest {
    private String firstName;
    private String lastName;
    private String middleName;

    private String emailAddress;
    private String address;
    private String idNumber;
    private String phoneNumber;
    private String accountType;
    private int depositAmount;

}
