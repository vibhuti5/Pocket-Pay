package com.pocketpay.transaction.dto;

import com.pocketpay.transaction.entity.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipientDto {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private BankAccountDto bankAccount;
}
