package com.pocketpay.transaction.service;

import com.pocketpay.transaction.dto.TransactionDto;
import com.pocketpay.transaction.entity.Transaction;
import com.pocketpay.transaction.enums.Status;
import com.pocketpay.transaction.enums.TransferType;
import com.pocketpay.transaction.exception.NotFoundException;
import com.pocketpay.transaction.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllTransactions() {
        // Arrange
        Transaction transaction1 = new Transaction();
        transaction1.setId(1);
        transaction1.setAmountSent(new BigDecimal("100.00"));

        Transaction transaction2 = new Transaction();
        transaction2.setId(2);
        transaction2.setAmountSent(new BigDecimal("200.00"));

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionRepository.findAll()).thenReturn(transactions);

        // Act
        List<TransactionDto> result = transactionService.getAllTransactions();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(new BigDecimal("100.00"), result.get(0).getAmountSent());
        assertEquals(2, result.get(1).getId());
        assertEquals(new BigDecimal("200.00"), result.get(1).getAmountSent());
    }

    @Test
    void testUpdateTransaction_WithExistingTransaction_Success() {
        // Arrange
        int transactionId = 1;
        TransactionDto transactionDto = TransactionDto.builder()
                .id(1)
                .amountSent(new BigDecimal("100.00"))
                .paymentPurpose("Test Payment")
                .transferRate(new BigDecimal("1.5"))
                .fromCurrency("USD")
                .toCurrency("EUR")
                .transferType(TransferType.debit)
                .status(Status.sending)
                .build();

        Transaction existingTransaction = new Transaction();
        existingTransaction.setId(transactionDto.getId());
        existingTransaction.setAmountSent(new BigDecimal("50.00"));

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        // Act
        TransactionDto updatedTransactionDto = transactionService.updateTransaction(transactionId, transactionDto);

        // Assert
        assertNotNull(updatedTransactionDto);
        assertEquals(transactionDto.getId(), updatedTransactionDto.getId());
        assertEquals(transactionDto.getAmountSent(), updatedTransactionDto.getAmountSent());
        assertEquals(transactionDto.getPaymentPurpose(), updatedTransactionDto.getPaymentPurpose());
        assertEquals(transactionDto.getTransferRate(), updatedTransactionDto.getTransferRate());
        assertEquals(transactionDto.getFromCurrency(), updatedTransactionDto.getFromCurrency());
        assertEquals(transactionDto.getToCurrency(), updatedTransactionDto.getToCurrency());
        assertEquals(transactionDto.getTransferType(), updatedTransactionDto.getTransferType());
        assertEquals(transactionDto.getStatus(), updatedTransactionDto.getStatus());

        // Verify that the repository's save method was called for Transaction
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void testUpdateTransaction_WithNonExistingTransaction_ThrowsNotFoundException() {
        // Arrange
        int transactionId = 1;
        TransactionDto transactionDto = TransactionDto.builder()
                .id(1)
                .amountSent(new BigDecimal("100.00"))
                .build();

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> {
            transactionService.updateTransaction(transactionId, transactionDto);
        });

        // Verify that the repository's findById method was called
        verify(transactionRepository, times(1)).findById(transactionId);
        verifyNoMoreInteractions(transactionRepository);
    }
}

