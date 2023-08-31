package com.example.bankapp.service.impl;

import static com.example.bankapp.utils.AccountUtils.getAccountByNumber;
import static com.example.bankapp.utils.CustomerUtils.getCustomerById;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Customer;
import com.example.bankapp.model.Transfer;
import com.example.bankapp.repository.CustomerRepo;
import com.example.bankapp.service.TransferService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@Slf4j
public class TransferServiceImpl implements TransferService {

  private static Map<Integer, List<Transfer>> transferMap = new HashMap<>();
  @Autowired
  private CustomerRepo customerRepo;

  public void transfer(Transfer transfer) throws Exception {
    Customer sender = getCustomerById(customerRepo.getCustomerList(), transfer.getSenderId());
    Customer receiver = getCustomerById(customerRepo.getCustomerList(), transfer.getReceiverId());
    Account senderAccount = getAccountByNumber(sender, transfer.getSenderAccountId());
    Account receiverAccount = getAccountByNumber(receiver, transfer.getReceiverAccountId());
    if (transfer.getAmount() >= 0) {
      senderAccount.debit(transfer.getAmount());
      receiverAccount.credit(transfer.getAmount());
      if (transferMap.containsKey(transfer.getSenderAccountId())) {
        transferMap.get(transfer.getSenderAccountId()).add(transfer);
      } else {
        List<Transfer> transferList = new ArrayList<>();
        transferList.add(transfer);
        transferMap.put(transfer.getSenderAccountId(), transferList);
      }
    } else {
      log.error("Transfer amount should not be negative");
      throw new Exception("Transfer amount should not be negative");
    }
  }

  public List<Transfer> getTransferHistoryForAccount(int accountId) throws Exception {
    if (transferMap.containsKey(accountId)) {
      return transferMap.get(accountId);
    } else {
      log.error("There is no account with provided id.");
      throw new Exception("There is no account with provided id.");
    }
  }
}
