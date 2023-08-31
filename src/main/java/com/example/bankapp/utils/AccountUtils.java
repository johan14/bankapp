package com.example.bankapp.utils;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Customer;

public class AccountUtils {

  public static Account getAccountByNumber(Customer customer, long accountNumber) {
    return customer.getAccountList().stream()
        .filter(account -> account.getAccountNo() == accountNumber)
        .findFirst()
        .orElseThrow();
  }

}
