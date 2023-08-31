package com.example.bankapp.controller;

import com.example.bankapp.BankAppApplication;
import com.example.bankapp.model.Account;
import com.example.bankapp.model.AccountRequest;
import com.example.bankapp.model.Customer;
import com.example.bankapp.model.Transfer;
import com.example.bankapp.repository.CustomerRepo;
import com.example.bankapp.service.AccountService;
import com.example.bankapp.service.TransferService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.util.Assert;

@SpringBootTest(classes = BankAppApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BankControllerIT {

  private final String baseUrl = "http://localhost:";
  @LocalServerPort
  private int port;
  @Autowired
  private TestRestTemplate restTemplate;
  @Autowired
  private CustomerRepo customerRepo;
  @Autowired
  private AccountService accountService;
  @Autowired
  private TransferService transferService;

  @Test
  public void addCustomer() {

    Customer customer = new Customer();
    customer.setId(1);
    customer.setName("Bill");

    ResponseEntity<Customer> response = restTemplate.postForEntity(
        baseUrl + port + "/addCustomer", customer, Customer.class);

    Assertions.assertNotNull(response.getBody());
    Assertions.assertEquals(customer.getId(), response.getBody().getId());
    Assertions.assertEquals(customer.getName(), response.getBody().getName());
  }

  @Test
  public void addAccount() {

    Customer customer = new Customer();
    customer.setId(1);
    customer.setName("Bill");

    customerRepo.addCustomer(customer);

    AccountRequest accountRequest = new AccountRequest();
    accountRequest.setCustomerId(1);
    accountRequest.setAccountNo(1);
    accountRequest.setBalance(100.0);

    ResponseEntity<Account> response = restTemplate.postForEntity(
        baseUrl + port + "/addAccount", accountRequest, Account.class);

    Assertions.assertNotNull(response.getBody());
    Assert.notEmpty(customer.getAccountList(), "Collection must contain elements");
    Assertions.assertEquals(customer.getAccountList().get(0).getAccountNo(),
        response.getBody().getAccountNo());
  }

  @Test
  public void addAccountGivenWrongCustomerThenThrows() {

    Customer customer = new Customer();
    customer.setId(1);
    customer.setName("Bill");

    customerRepo.addCustomer(customer);

    AccountRequest accountRequest = new AccountRequest();
    accountRequest.setCustomerId(2);
    accountRequest.setAccountNo(1);
    accountRequest.setBalance(100.0);

    ResponseEntity<String> response = restTemplate.postForEntity(
        baseUrl + port + "/addAccount", accountRequest, String.class);

    Assertions.assertNotNull(response.getBody());
    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void transfer() {

    Customer customer = Customer.builder().id(1).name("Bill").accountList(new ArrayList<>())
        .build();
    Account account = Account.builder().accountNo(1).balance(100).build();
    customer.getAccountList().add(account);

    Customer customer2 = Customer.builder().id(2).name("James").accountList(new ArrayList<>())
        .build();
    Account account2 = Account.builder().accountNo(2).balance(100).build();
    customer2.getAccountList().add(account2);

    customerRepo.addCustomer(customer);
    customerRepo.addCustomer(customer2);

    Transfer transfer = Transfer.builder().senderId(1).senderAccountId(1).receiverId(2)
        .receiverAccountId(2).amount(50).build();

    ResponseEntity<String> response = restTemplate.postForEntity(
        baseUrl + port + "/transfer", transfer, String.class);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(50, account.getBalance());
    Assertions.assertEquals(150, account2.getBalance());

  }

  @Test
  public void transferGivenInvalidAmountThenThrows() {

    Customer customer = Customer.builder().id(1).name("Bill").accountList(new ArrayList<>())
        .build();
    Account account = Account.builder().accountNo(1).balance(100).build();
    customer.getAccountList().add(account);

    Customer customer2 = Customer.builder().id(2).name("James").accountList(new ArrayList<>())
        .build();
    Account account2 = Account.builder().accountNo(2).balance(100).build();
    customer2.getAccountList().add(account2);

    customerRepo.addCustomer(customer);
    customerRepo.addCustomer(customer2);

    Transfer transfer = Transfer.builder().senderId(1).senderAccountId(1).receiverId(2)
        .receiverAccountId(2).amount(-50).build();

    ResponseEntity<String> response = restTemplate.postForEntity(
        baseUrl + port + "/transfer", transfer, String.class);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

  }

  @Test
  public void balances() throws Exception {

    Customer customer = Customer.builder().id(1).name("Bill").accountList(new ArrayList<>())
        .build();
    Account account = Account.builder().accountNo(1).balance(100).build();
    customer.getAccountList().add(account);

    customerRepo.addCustomer(customer);
    accountService.addAccount(
        AccountRequest.builder().balance(account.getBalance()).accountNo(1).customerId(1).build());

    final int accountNo = 1;

    ResponseEntity<Account> response = restTemplate.getForEntity(
        baseUrl + port + "/balances?accountId=" + accountNo, Account.class);

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getBody());
    Assertions.assertEquals(accountNo, response.getBody().getAccountNo());
    Assertions.assertEquals(100, response.getBody().getBalance());

  }

  @Test
  public void transferHistory() throws Exception {

    Customer customer = Customer.builder().id(1).name("Bill").accountList(new ArrayList<>())
        .build();
    Account account = Account.builder().accountNo(1).balance(100).build();
    customer.getAccountList().add(account);

    Customer customer2 = Customer.builder().id(2).name("James").accountList(new ArrayList<>())
        .build();
    Account account2 = Account.builder().accountNo(2).balance(100).build();
    customer2.getAccountList().add(account2);

    customerRepo.addCustomer(customer);
    customerRepo.addCustomer(customer2);

    Transfer transfer = Transfer.builder().senderId(1).senderAccountId(1).receiverId(2)
        .receiverAccountId(2).amount(50).build();
    transferService.transfer(transfer);

    final int ACCOUNT_ID = 1;

    ResponseEntity<List> response = restTemplate.getForEntity(
        baseUrl + port + "/transferHistory?accountId=" + ACCOUNT_ID, List.class);

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getBody());
    Assert.notEmpty(response.getBody(), "Collection must not be empty");
    Assertions.assertEquals(transfer.getSenderAccountId(),
        ((Map<?, ?>) response.getBody().get(0)).get("senderId"));

  }


}
