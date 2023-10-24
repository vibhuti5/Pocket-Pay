package com.pocketpay.transaction.service;

import com.pocketpay.transaction.dto.RecipientDto;
import com.pocketpay.transaction.entity.BankAccount;
import com.pocketpay.transaction.entity.Recipient;
import com.pocketpay.transaction.exception.InternalServerException;
import com.pocketpay.transaction.exception.PostException;
import com.pocketpay.transaction.repository.BankAccountRepository;
import com.pocketpay.transaction.repository.RecipientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipientServiceImpl implements RecipientService {

    @Autowired
    private RecipientRepository recipientRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;
    private ModelMapper modelMapper;

    public RecipientServiceImpl() {
        modelMapper = new ModelMapper();
    }

    @Override
    public List<RecipientDto> getAllRecipients() {
        try {
            List<Recipient> recipients = recipientRepository.findAll();
            return recipients.stream()
                    .map(recipient -> modelMapper.map(recipient, RecipientDto.class))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new InternalServerException("Error occurred while fetching recipients");
        }
    }

    @Override
    public RecipientDto saveRecipient(RecipientDto recipientDto) {
        try {
            Recipient recipient = modelMapper.map(recipientDto, Recipient.class);
            if (recipientDto.getBankAccount() != null) {
                BankAccount bankAccount = modelMapper.map(recipientDto.getBankAccount(), BankAccount.class);
                // Save the BankAccount object before setting it in the Recipient
                BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
                recipient.setBankAccount(savedBankAccount);
            }
            Recipient savedRecipient = recipientRepository.save(recipient);
            RecipientDto savedRecipientDto = modelMapper.map(savedRecipient, RecipientDto.class);
            return savedRecipientDto;
        } catch (Exception ex) {
            throw new PostException("Error occurred while saving recipient", ex);
        }
    }
}