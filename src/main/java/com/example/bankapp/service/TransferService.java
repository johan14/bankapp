package com.example.bankapp.service;

import com.example.bankapp.model.Transfer;
import java.util.List;

public interface TransferService {

  public void transfer(Transfer transfer) throws Exception;

  public List<Transfer> getTransferHistoryForAccount(int accountId) throws Exception;

}
