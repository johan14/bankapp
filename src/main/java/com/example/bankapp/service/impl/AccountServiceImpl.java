package com.example.bankapp.service.impl;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.AccountRequest;
import com.example.bankapp.model.Customer;
import com.example.bankapp.repository.CustomerRepo;
import com.example.bankapp.service.AccountService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

  private static final Map<Integer, Account> accountMap = new HashMap<>();
  @Autowired
  private CustomerRepo customerRepo;

  @Value("${app.balance}")
  private double defaultBalance;


  public Account addAccount(AccountRequest accountRequest) throws Exception {
    Optional<Customer> customerOptional = customerRepo.getCustomerList().stream()
        .filter(customer -> accountRequest.getCustomerId() == customer.getId()).findFirst();
    if (customerOptional.isPresent()) {
      Customer customer = customerOptional.get();
      if (accountRequest.getBalance() == null || accountRequest.getBalance() < 0) {
        accountRequest.setBalance(defaultBalance);
      }
      Account account = Account.builder().accountNo(accountRequest.getAccountNo())
          .balance(accountRequest.getBalance()).build();
      customer.getAccountList().add(account);
      accountMap.put(account.getAccountNo(), account);
      return account;
    } else {
      log.error("Customer with this id is not present");
      throw new Exception("Customer with this id is not present");
    }
  }

  public Account getAccount(int accountId) throws Exception {
    if (accountMap.containsKey(accountId)) {
      return accountMap.get(accountId);
    } else {
      log.error("No account with this id");
      throw new Exception("No account with this id");
    }
  }
}
