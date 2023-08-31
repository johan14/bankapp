package com.example.bankapp.utils;

import com.example.bankapp.model.Customer;
import java.util.List;

public class CustomerUtils {

  public static Customer getCustomerById(List<Customer> customerList, long customerId) {
    return customerList.stream()
        .filter(customer -> customer.getId() == customerId)
        .findFirst()
        .orElseThrow();
  }

}
