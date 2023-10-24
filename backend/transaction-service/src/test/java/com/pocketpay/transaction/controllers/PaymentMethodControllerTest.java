package com.pocketpay.transaction.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketpay.transaction.dto.PaymentMethodDto;
import com.pocketpay.transaction.enums.PayMethodMode;
import com.pocketpay.transaction.service.PaymentMethodService;
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

class PaymentMethodControllerTest {
    @Mock
    private PaymentMethodService paymentMethodService;

    @InjectMocks
    private PaymentMethodController paymentMethodController;

    private ObjectMapper objectMapper;

    {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFindAllPaymentMethods_Success() {
        // Arrange
        List<PaymentMethodDto> paymentMethodDtos = new ArrayList<>();
        paymentMethodDtos.add(PaymentMethodDto.builder()
                .id(1)
                .userId(123)
                .payMethodMode(PayMethodMode.debit)
                .bankAccount(456)
                .debitCard(789)
                .build());

        when(paymentMethodService.getAllPayments()).thenReturn(paymentMethodDtos);

        // Act
        ResponseEntity<List<PaymentMethodDto>> responseEntity = paymentMethodController.findAllPaymentMethods();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(paymentMethodDtos, responseEntity.getBody());

        // Verify that the service method was called once
        verify(paymentMethodService, times(1)).getAllPayments();
        verifyNoMoreInteractions(paymentMethodService);
    }

    @Test
    public void testSavePaymentMethods_Success() {
        // Arrange
        PaymentMethodDto inputDto = PaymentMethodDto.builder()
                .id(1)
                .userId(123)
                .payMethodMode(PayMethodMode.debit)
                .bankAccount(456)
                .debitCard(789)
                .build();

        when(paymentMethodService.savePaymentMethod(any(PaymentMethodDto.class))).thenReturn(inputDto);

        // Act
        ResponseEntity<PaymentMethodDto> responseEntity = paymentMethodController.savePaymentMethods(inputDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(inputDto, responseEntity.getBody());

        // Verify that the service method was called once with the input DTO
        verify(paymentMethodService, times(1)).savePaymentMethod(inputDto);
        verifyNoMoreInteractions(paymentMethodService);
    }

}