package com.example.bankapp.repository;

import com.example.bankapp.model.Customer;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class CustomerRepo {

  private List<Customer> customerList = new ArrayList<>();

  public Customer addCustomer(Customer customer) {
    customerList.add(customer);
    return customer;
  }
}
