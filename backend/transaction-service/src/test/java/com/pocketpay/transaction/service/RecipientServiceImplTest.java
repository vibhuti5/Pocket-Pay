package com.pocketpay.transaction.service;

import com.pocketpay.transaction.dto.RecipientDto;
import com.pocketpay.transaction.entity.Recipient;
import com.pocketpay.transaction.exception.InternalServerException;
import com.pocketpay.transaction.exception.PostException;
import com.pocketpay.transaction.repository.BankAccountRepository;
import com.pocketpay.transaction.repository.RecipientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RecipientServiceImplTest {
    @Mock
    private RecipientRepository recipientRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private RecipientServiceImpl recipientService;

    {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGetAllRecipients_EmptyList_Success() {
        // Arrange
        List<Recipient> recipients = new ArrayList<>();

        when(recipientRepository.findAll()).thenReturn(recipients);

        // Act
        List<RecipientDto> recipientDtos = recipientService.getAllRecipients();

        // Assert
        assertNotNull(recipientDtos);
        assertEquals(0, recipientDtos.size());

        // Verify that the repository's findAll method was called
        verify(recipientRepository, times(1)).findAll();
        verifyNoMoreInteractions(recipientRepository);
    }

    @Test
    public void testSaveRecipient_Exception() {
        // Arrange
        RecipientDto recipientDto = RecipientDto.builder()
                .id(3)
                .email("recipient@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        when(recipientRepository.save(any(Recipient.class))).thenThrow(new RuntimeException("Test Exception"));

        // Act & Assert
        assertThrows(PostException.class, () -> {
            recipientService.saveRecipient(recipientDto);
        });

        // Verify that the repository's save method was called
        verify(recipientRepository, times(1)).save(any(Recipient.class));
        verifyNoMoreInteractions(recipientRepository);
    }
}

