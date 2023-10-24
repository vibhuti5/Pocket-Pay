package com.pocketpay.transaction.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketpay.transaction.dto.DebitCardDto;
import com.pocketpay.transaction.exception.NotFoundException;
import com.pocketpay.transaction.service.DebitCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class DebitCardControllerTest {
    @Mock
    private DebitCardService debitCardService;

    @InjectMocks
    private DebitCardController debitCardController;

    private ObjectMapper objectMapper;

    {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
    }


    @Test
    public void testUpdateDebitCard_NotFound() {
        // Arrange
        int debitCardId = 1;
        DebitCardDto inputDto = DebitCardDto.builder()
                .id(debitCardId)
                .cvv("123")
                .expiryDate(LocalDate.of(2023, 12, 31))
                .bankAccount(123456789)
                .build();

        when(debitCardService.updateDebitCard(eq(debitCardId), any(DebitCardDto.class)))
                .thenThrow(new NotFoundException("Debit Card not found"));

        // Act
        ResponseEntity<DebitCardDto> responseEntity = debitCardController.updateDebitCard(debitCardId, inputDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        // Verify that the service method was called once with the input DTO
        verify(debitCardService, times(1)).updateDebitCard(debitCardId, inputDto);
        verifyNoMoreInteractions(debitCardService);
    }
}
