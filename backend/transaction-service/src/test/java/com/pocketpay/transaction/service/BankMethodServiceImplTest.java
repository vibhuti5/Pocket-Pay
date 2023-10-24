package com.pocketpay.transaction.service;

import com.pocketpay.transaction.dto.BankAccountDto;
import com.pocketpay.transaction.enums.AccountType;
import com.pocketpay.transaction.entity.BankAccount;
import com.pocketpay.transaction.exception.PostException;
import com.pocketpay.transaction.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class BankMethodServiceImplTest {
    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankMethodServiceImpl bankMethodService;

    private BankAccountDto bankAccountDto;

   {
        MockitoAnnotations.initMocks(this);

        // Create a sample BankAccountDto for testing
        bankAccountDto = BankAccountDto.builder()
                .accountNumber(1234567890)
                .bankName("Sample Bank")
                .bankAddress("123 Main St")
                .ifsc("ABCD123456")
                .accountType(AccountType.Checking)
                .build();
    }

    @Test
    public void testSaveBankAccount_Success() {
        // Arrange
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(bankAccountDto.getAccountNumber());
        bankAccount.setBankName(bankAccountDto.getBankName());
        bankAccount.setBankAddress(bankAccountDto.getBankAddress());
        bankAccount.setIfsc(bankAccountDto.getIfsc());
        bankAccount.setAccountType(bankAccountDto.getAccountType());

        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccount);

        // Act
        BankAccountDto savedBankAccountDto = bankMethodService.saveBankAccount(bankAccountDto);

        // Assert
        assertNotNull(savedBankAccountDto);
        assertEquals(bankAccountDto, savedBankAccountDto);

        // Verify that the repository's save method was called once
        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
        verifyNoMoreInteractions(bankAccountRepository);
    }

}

