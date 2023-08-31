package com.example.bankapp.controller;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.AccountRequest;
import com.example.bankapp.model.Customer;
import com.example.bankapp.model.Transfer;
import com.example.bankapp.repository.CustomerRepo;
import com.example.bankapp.service.AccountService;
import com.example.bankapp.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Bank API")
public class BankController {

  @Autowired
  private AccountService accountService;
  @Autowired
  private TransferService transferService;
  @Autowired
  private CustomerRepo customerRepo;

  @PostMapping("addCustomer")
  @Operation(description = "Adds a customer")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully added")
  })
  public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
    return ResponseEntity.ok(customerRepo.addCustomer(customer));
  }

  @PostMapping("addAccount")
  @Operation(description = "Adds an account to a customer")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully added"),
      @ApiResponse(responseCode = "500", description = "Error when adding")
  })
  public ResponseEntity<Account> addAccount(@RequestBody AccountRequest accountRequest)
      throws Exception {
    return ResponseEntity.ok(accountService.addAccount(accountRequest));
  }

  @PostMapping("transfer")
  @Operation(description = "Transfers amount between two accounts")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully transfered"),
      @ApiResponse(responseCode = "500", description = "Error when transferring")
  })
  public ResponseEntity<String> transfer(@RequestBody Transfer transfer) throws Exception {
    transferService.transfer(transfer);
    return ResponseEntity.ok("Transfer completed");
  }

  @GetMapping("balances")
  @Operation(description = "Retrieves balance for a given account")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved account balances"),
      @ApiResponse(responseCode = "500", description = "Error when retrieving account balances")
  })
  public ResponseEntity<Account> getAccount(@RequestParam int accountId) throws Exception {
    return ResponseEntity.ok(accountService.getAccount(accountId));
  }

  @GetMapping("transferHistory")
  @Operation(description = "Gets transfer history for a given account")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved transfer history"),
      @ApiResponse(responseCode = "500", description = "Error when retrieving transfer history for an account")
  })
  public ResponseEntity<List<Transfer>> getTransferHistoryForAccount(@RequestParam int accountId)
      throws Exception {
    return ResponseEntity.ok(transferService.getTransferHistoryForAccount(accountId));
  }
}
