package com.pocketpay.transaction.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketpay.transaction.dto.BankAccountDto;
import com.pocketpay.transaction.enums.AccountType;
import com.pocketpay.transaction.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class BankAccountControllerTest {
    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private BankAccountController bankAccountController;

    private ObjectMapper objectMapper;

    {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSaveBankAccount_Success() {
        // Arrange
        BankAccountDto inputDto = BankAccountDto.builder()
                .accountNumber(123456789)
                .bankName("Test Bank")
                .bankAddress("Test Address")
                .ifsc("TEST12345")
                .accountType(AccountType.Saving)
                .build();

        when(bankAccountService.saveBankAccount(any(BankAccountDto.class))).thenReturn(inputDto);

        // Act
        ResponseEntity<BankAccountDto> responseEntity = bankAccountController.saveBankAccount(inputDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(inputDto, responseEntity.getBody());

        // Verify that the service method was called once with the input DTO
        verify(bankAccountService, times(1)).saveBankAccount(inputDto);
        verifyNoMoreInteractions(bankAccountService);
    }

}
