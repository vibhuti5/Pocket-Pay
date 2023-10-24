package com.pocketpay.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DebitCardDto {
    private int id;
    private String cvv;
    private LocalDate expiryDate;
    private int bankAccount;
}
