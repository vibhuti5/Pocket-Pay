package com.pocketpay.transaction.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketpay.transaction.dto.TransactionDto;
import com.pocketpay.transaction.enums.Status;
import com.pocketpay.transaction.exception.NotFoundException;
import com.pocketpay.transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionControllerTest {
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private ObjectMapper objectMapper;

    {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFindAllTransactions_Success() {
        // Arrange
        List<TransactionDto> transactionDtos = new ArrayList<>();
        transactionDtos.add(TransactionDto.builder()
                .id(1)
                .amountSent(new BigDecimal("100.00"))
                .fromCurrency("USD")
                .toCurrency("EUR")
                .status(Status.sending)
                .build());

        when(transactionService.getAllTransactions()).thenReturn(transactionDtos);

        // Act
        ResponseEntity<List<TransactionDto>> responseEntity = transactionController.findAllTransactions();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(transactionDtos, responseEntity.getBody());

        // Verify that the service method was called once
        verify(transactionService, times(1)).getAllTransactions();
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    public void testSaveTransactions_Success() {
        // Arrange
        TransactionDto inputDto = TransactionDto.builder()
                .amountSent(new BigDecimal("200.00"))
                .fromCurrency("EUR")
                .toCurrency("USD")
                .status(Status.cancelled)
                .build();

        when(transactionService.saveTransaction(any(TransactionDto.class))).thenReturn(inputDto);

        // Act
        ResponseEntity<TransactionDto> responseEntity = transactionController.saveTransactions(inputDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(inputDto, responseEntity.getBody());

        // Verify that the service method was called once with the input DTO
        verify(transactionService, times(1)).saveTransaction(inputDto);
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    public void testUpdateTransaction_Success() {
        // Arrange
        int transactionId = 1;
        TransactionDto inputDto = TransactionDto.builder()
                .id(transactionId)
                .amountSent(new BigDecimal("300.00"))
                .fromCurrency("GBP")
                .toCurrency("JPY")
                .status(Status.sent)
                .build();

        when(transactionService.updateTransaction(eq(transactionId), any(TransactionDto.class))).thenReturn(inputDto);

        // Act
        ResponseEntity<TransactionDto> responseEntity = transactionController.updateTransaction(transactionId, inputDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inputDto, responseEntity.getBody());

        // Verify that the service method was called once with the input DTO and ID
        verify(transactionService, times(1)).updateTransaction(transactionId, inputDto);
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    public void testUpdateTransaction_NotFound() {
        // Arrange
        int transactionId = 1;
        TransactionDto inputDto = TransactionDto.builder()
                .id(transactionId)
                .amountSent(new BigDecimal("300.00"))
                .fromCurrency("GBP")
                .toCurrency("JPY")
                .status(Status.sending)
                .build();

        // Simulate NotFoundException being thrown by the service
        when(transactionService.updateTransaction(eq(transactionId), any(TransactionDto.class)))
                .thenThrow(new NotFoundException("Transaction not found"));

        // Act
        ResponseEntity<TransactionDto> responseEntity = transactionController.updateTransaction(transactionId, inputDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        // Verify that the service method was called once with the input DTO and ID
        verify(transactionService, times(1)).updateTransaction(transactionId, inputDto);
        verifyNoMoreInteractions(transactionService);
    }
}

