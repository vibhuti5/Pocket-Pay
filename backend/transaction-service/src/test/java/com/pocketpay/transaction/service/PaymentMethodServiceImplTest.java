package com.pocketpay.transaction.service;

import com.pocketpay.transaction.dto.PaymentMethodDto;
import com.pocketpay.transaction.entity.BankAccount;
import com.pocketpay.transaction.entity.DebitCard;
import com.pocketpay.transaction.entity.PaymentMethod;
import com.pocketpay.transaction.enums.PayMethodMode;
import com.pocketpay.transaction.exception.PostException;
import com.pocketpay.transaction.repository.PaymentMethodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentMethodServiceImplTest {
    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @InjectMocks
    private PaymentMethodServiceImpl paymentMethodService;

    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSavePaymentMethod_Success() {
        // Arrange
        PaymentMethodDto paymentMethodDto = PaymentMethodDto.builder()
                .id(1)
                .userId(1001)
                .payMethodMode(PayMethodMode.debit)
                .bankAccount(123456789)
                .debitCard(987654321)
                .build();

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(paymentMethodDto.getId());
        paymentMethod.setUserId(paymentMethodDto.getUserId());
        paymentMethod.setPayMethodMode(paymentMethodDto.getPayMethodMode());

        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(paymentMethodDto.getBankAccount());
        paymentMethod.setBankAccount(bankAccount);

        DebitCard debitCard = new DebitCard();
        debitCard.setId(paymentMethodDto.getDebitCard());
        paymentMethod.setDebitCard(debitCard);

        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);

        // Act
        PaymentMethodDto savedPaymentMethodDto = paymentMethodService.savePaymentMethod(paymentMethodDto);

        // Assert
        assertNotNull(savedPaymentMethodDto);
        assertEquals(paymentMethodDto, savedPaymentMethodDto);

        // Verify that the repository's save method was called
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
        verifyNoMoreInteractions(paymentMethodRepository);
    }


    @Test
    public void testGetAllPayments_EmptyList_Success() {
        // Arrange
        List<PaymentMethod> paymentMethods = new ArrayList<>();

        when(paymentMethodRepository.findAll()).thenReturn(paymentMethods);

        // Act
        List<PaymentMethodDto> paymentMethodDtos = paymentMethodService.getAllPayments();

        // Assert
        assertNotNull(paymentMethodDtos);
        assertEquals(0, paymentMethodDtos.size());

        // Verify that the repository's findAll method was called
        verify(paymentMethodRepository, times(1)).findAll();
        verifyNoMoreInteractions(paymentMethodRepository);
    }
}

