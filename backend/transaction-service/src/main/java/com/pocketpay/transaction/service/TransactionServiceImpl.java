package com.pocketpay.transaction.service;

import com.pocketpay.transaction.dto.TransactionDto;
import com.pocketpay.transaction.entity.*;
import com.pocketpay.transaction.exception.InternalServerException;
import com.pocketpay.transaction.exception.NotFoundException;
import com.pocketpay.transaction.exception.PostException;
import com.pocketpay.transaction.exception.TransactionException;
import com.pocketpay.transaction.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private RecipientRepository recipientRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private DebitCardRepository debitCardRepository;
    private ModelMapper modelMapper;

    public TransactionServiceImpl() {
        modelMapper = new ModelMapper();
    }

    @Override
    public List<TransactionDto> getAllTransactions() {

        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .collect(Collectors.toList());

    }
    @Override
    public TransactionDto saveTransaction(TransactionDto transactionDto) {
        try {
            Transaction transaction = modelMapper.map(transactionDto, Transaction.class);

            // Save the PaymentMethod object before setting it in the Transaction
            if (transactionDto.getPaymentMethod() != null) {
                PaymentMethod paymentMethod = modelMapper.map(transactionDto.getPaymentMethod(), PaymentMethod.class);
                BankAccount bankAccount = modelMapper.map(transactionDto.getPaymentMethod().getBankAccount(), BankAccount.class);
                DebitCard debitCard = modelMapper.map(transactionDto.getPaymentMethod().getDebitCard(),DebitCard.class);
                DebitCard savedDebitCard = debitCardRepository.save(debitCard);
                paymentMethod.setDebitCard(savedDebitCard);
                BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
                paymentMethod.setBankAccount(savedBankAccount);
                PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);
                transaction.setPaymentMethod(savedPaymentMethod);
            }
            if (transactionDto.getRecipient() != null){
                Recipient recipient = modelMapper.map(transactionDto.getRecipient(),Recipient.class);
                Recipient savedRecipient = recipientRepository.save(recipient);
                transaction.setRecipient(savedRecipient);
            }

            Transaction savedTransaction = transactionRepository.save(transaction);
            return modelMapper.map(savedTransaction, TransactionDto.class);
        } catch (Exception ex) {
            throw new PostException("Error occurred while saving transaction", ex);
        }
    }
    @Override
    public TransactionDto updateTransaction(int id, TransactionDto transactionDto) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);

        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            if (transactionDto.getAmountSent() != null) {
                transaction.setAmountSent(transactionDto.getAmountSent());
            }

            if (transactionDto.getTransferRate() != null) {
                transaction.setTransferRate(transactionDto.getTransferRate());
            }
            if (transactionDto.getPaymentPurpose() != null) {
                transaction.setPaymentPurpose(transactionDto.getPaymentPurpose());
            }
            if (transactionDto.getFromCurrency() != null) {
                transaction.setFromCurrency(transactionDto.getFromCurrency());
            }
            if (transactionDto.getToCurrency() != null) {
                transaction.setToCurrency(transactionDto.getToCurrency());
            }
            if (transactionDto.getTransferType() != null) {
                transaction.setTransferType(transactionDto.getTransferType());
            }
            if (transactionDto.getStatus() != null) {
                transaction.setStatus(transactionDto.getStatus());
            }

            try {
                Transaction updatedTransaction = transactionRepository.save(transaction);
                return modelMapper.map(updatedTransaction, TransactionDto.class);
            } catch (Exception ex) {
                throw new TransactionException("Error occurred while updating transaction", ex);
            }
        } else {
            throw new NotFoundException("Transaction not found with ID: " + id);
        }
    }

}
