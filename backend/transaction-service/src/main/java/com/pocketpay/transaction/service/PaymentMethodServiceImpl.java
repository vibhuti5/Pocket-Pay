package com.pocketpay.transaction.service;

import com.pocketpay.transaction.dto.PaymentMethodDto;
import com.pocketpay.transaction.entity.BankAccount;
import com.pocketpay.transaction.entity.DebitCard;
import com.pocketpay.transaction.entity.PaymentMethod;
import com.pocketpay.transaction.exception.PostException;
import com.pocketpay.transaction.exception.TransactionFailure;
import com.pocketpay.transaction.repository.PaymentMethodRepository;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    private ModelMapper modelMapper;

    public PaymentMethodServiceImpl() {
        modelMapper = new ModelMapper();
        configureModelMapper();
    }

    private void configureModelMapper() {
        PropertyMap<PaymentMethod, PaymentMethodDto> propertyMap = new PropertyMap<PaymentMethod, PaymentMethodDto>() {
            @Override
            protected void configure() {
                map().setBankAccount(source.getBankAccount().getAccountNumber());
                map().setDebitCard(source.getDebitCard().getId());
                map().setUserId(source.getUserId());
                map().setPayMethodMode(source.getPayMethodMode());
            }
        };

        modelMapper.addMappings(propertyMap);
    }

    @Override
    public PaymentMethodDto savePaymentMethod(PaymentMethodDto paymentMethodDto) {
        try {
            PaymentMethod paymentMethod = modelMapper.map(paymentMethodDto, PaymentMethod.class);

            if (paymentMethodDto.getBankAccount() != 0) {
                BankAccount bankAccount = new BankAccount();
                bankAccount.setAccountNumber(paymentMethodDto.getBankAccount());
                paymentMethod.setBankAccount(bankAccount);
            } else {
                paymentMethod.setBankAccount(null);
            }

            if (paymentMethodDto.getDebitCard() != 0) {
                DebitCard debitCard = new DebitCard();
                debitCard.setId(paymentMethodDto.getDebitCard());
                paymentMethod.setDebitCard(debitCard);
            } else {
                paymentMethod.setDebitCard(null);
            }

            PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);
            return modelMapper.map(savedPaymentMethod, PaymentMethodDto.class);
        } catch (Exception ex) {
            throw new PostException("Error occurred while saving payment method", ex);
        }
    }

    @Override
    public List<PaymentMethodDto> getAllPayments() {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();
        return paymentMethods.stream()
                .map(paymentMethod -> modelMapper.map(paymentMethod, PaymentMethodDto.class))
                .collect(Collectors.toList());
    }
}