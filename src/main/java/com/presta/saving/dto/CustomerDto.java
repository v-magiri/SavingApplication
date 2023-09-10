package com.presta.saving.dto;

import java.util.List;

public interface CustomerDto {
    String getMemberNumber();

    String getFirstName();
    String getLastName();
    String getMiddleName();
    String getIdNumber();

    String getEmailAddress();
    String getAddress();
    List<AccountView> getSavingAccounts();

    interface AccountView{

    }
}
