package com.pocketpay.transaction.service;

import com.pocketpay.transaction.dto.BankAccountDto;
import com.pocketpay.transaction.dto.DebitCardDto;
import com.pocketpay.transaction.entity.BankAccount;
import com.pocketpay.transaction.entity.DebitCard;
import com.pocketpay.transaction.exception.TransactionException;
import com.pocketpay.transaction.repository.DebitCardRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DebitCardServiceImpl implements DebitCardService {

    @Autowired
    private DebitCardRepository debitCardRepository;
    private ModelMapper modelMapper;

    public DebitCardServiceImpl() {
        modelMapper = new ModelMapper();
    }

    @Override
    public DebitCardDto updateDebitCard(int id, DebitCardDto debitCardDto) {
        Optional<DebitCard> optionalDebitCard = debitCardRepository.findById(id);

        DebitCard debitCard = optionalDebitCard.orElseThrow(() -> new TransactionException("Debit card not found"));

        if (debitCardDto.getExpiryDate() != null) {
            debitCard.setExpiryDate(debitCardDto.getExpiryDate());
        }

        if (debitCardDto.getCvv() != null) {
            debitCard.setCvv(debitCardDto.getCvv());
        }

        if (debitCardDto.getBankAccount() != 0) {
            BankAccount bankAccount = debitCard.getBankAccount();
            bankAccount.setAccountNumber(debitCardDto.getBankAccount());
            debitCard.setBankAccount(bankAccount);
        }

        DebitCard updatedDebitCard = debitCardRepository.save(debitCard);

        PropertyMap<DebitCard, DebitCardDto> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setBankAccount(source.getBankAccount().getAccountNumber());
            }
        };

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(propertyMap);

        return modelMapper.map(updatedDebitCard, DebitCardDto.class);
    }
}
