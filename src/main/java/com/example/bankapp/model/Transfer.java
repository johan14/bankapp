package com.example.bankapp.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Transfer {

  private int senderId;
  private int senderAccountId;
  private int receiverId;
  private int receiverAccountId;
  private Date date = new Date();
  private double amount;
}
