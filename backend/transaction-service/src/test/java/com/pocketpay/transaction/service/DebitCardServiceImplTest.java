package com.pocketpay.transaction.service;

import com.pocketpay.transaction.dto.DebitCardDto;
import com.pocketpay.transaction.entity.BankAccount;
import com.pocketpay.transaction.entity.DebitCard;
import com.pocketpay.transaction.exception.TransactionException;
import com.pocketpay.transaction.repository.DebitCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class DebitCardServiceImplTest {
    @Mock
    private DebitCardRepository debitCardRepository;

    @InjectMocks
    private DebitCardServiceImpl debitCardService;

    private DebitCardDto debitCardDto;

   {
        MockitoAnnotations.initMocks(this);

        // Create a sample DebitCardDto for testing
        debitCardDto = DebitCardDto.builder()
                .id(1)
                .cvv("123")
                .expiryDate(LocalDate.of(2023, 12, 31))
                .bankAccount(123456789)
                .build();
    }

    @Test
    public void testUpdateDebitCard_NotFound() {
        // Arrange
        when(debitCardRepository.findById(debitCardDto.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TransactionException.class, () -> {
            debitCardService.updateDebitCard(debitCardDto.getId(), debitCardDto);
        });

        // Verify that the repository's findById method was called
        verify(debitCardRepository, times(1)).findById(debitCardDto.getId());
        verifyNoMoreInteractions(debitCardRepository);
    }
}

