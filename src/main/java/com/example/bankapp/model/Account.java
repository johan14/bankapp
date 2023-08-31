package com.example.bankapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

  private int accountNo;
  private double balance;

  public void credit(double amount) {
    balance += amount;
  }

  public void debit(double amount) throws Exception {
    if (amount > balance) {
      log.error("You don't have enough balance");
      throw new Exception("You don't have enough balance");
    } else {
      balance -= amount;
    }
  }
}
