package com.pocketpay.transaction.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketpay.transaction.dto.RecipientDto;
import com.pocketpay.transaction.service.RecipientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class RecipientControllerTest {
    @Mock
    private RecipientService recipientService;

    @InjectMocks
    private RecipientController recipientController;

    private ObjectMapper objectMapper;

    {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFindAllRecipients_Success() {
        // Arrange
        List<RecipientDto> recipientDtos = new ArrayList<>();
        recipientDtos.add(RecipientDto.builder()
                .id(1)
                .email("recipient1@example.com")
                .firstName("John")
                .lastName("Doe")
                .build());

        when(recipientService.getAllRecipients()).thenReturn(recipientDtos);

        // Act
        ResponseEntity<List<RecipientDto>> responseEntity = recipientController.findAllRecipients();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(recipientDtos, responseEntity.getBody());

        // Verify that the service method was called once
        verify(recipientService, times(1)).getAllRecipients();
        verifyNoMoreInteractions(recipientService);
    }

    @Test
    public void testSaveRecipients_Success() {
        // Arrange
        RecipientDto inputDto = RecipientDto.builder()
                .id(1)
                .email("recipient2@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .build();

        when(recipientService.saveRecipient(any(RecipientDto.class))).thenReturn(inputDto);

        // Act
        ResponseEntity<RecipientDto> responseEntity = recipientController.saveRecipients(inputDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(inputDto, responseEntity.getBody());

        // Verify that the service method was called once with the input DTO
        verify(recipientService, times(1)).saveRecipient(inputDto);
        verifyNoMoreInteractions(recipientService);
    }
}

