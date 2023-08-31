package com.example.bankapp.service;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.AccountRequest;

public interface AccountService {

  public Account addAccount(AccountRequest accountRequest) throws Exception;

  public Account getAccount(int accountId) throws Exception;

}
