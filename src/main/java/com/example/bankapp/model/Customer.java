package com.example.bankapp.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Customer {

  private int id;
  private String name;
  private List<Account> accountList = new ArrayList<>();
}
