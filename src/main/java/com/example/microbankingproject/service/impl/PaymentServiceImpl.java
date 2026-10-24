package com.example.microbankingproject.service.impl;

import com.example.microbankingproject.dto.request.PaymentRequestDto;
import com.example.microbankingproject.dto.response.PaymentResponseDto;
import com.example.microbankingproject.entity.AccountEntity;
import com.example.microbankingproject.entity.CardEntity;
import com.example.microbankingproject.entity.TransactionEntity;
import com.example.microbankingproject.entity.enums.PaymentType;
import com.example.microbankingproject.entity.enums.Status;
import com.example.microbankingproject.entity.enums.TransactionStatus;
import com.example.microbankingproject.exception.ApplicationException;
import com.example.microbankingproject.exception.CommonErrors;
import com.example.microbankingproject.repository.AccountRepository;
import com.example.microbankingproject.repository.CardRepository;
import com.example.microbankingproject.repository.TransactionRepository;
import com.example.microbankingproject.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PaymentServiceImpl implements PaymentService {

    CardRepository cardRepository;
    AccountRepository accountRepository;
    TransactionRepository transactionRepository;

    private static final long RACE_WINDOW_MS = 10000L;

    @Override
    @Transactional
    public PaymentResponseDto payUnsafe(PaymentRequestDto paymentRequestDto) {
        log.info("Unsafe Payment Started...");

        CardEntity card = getCardByNumber(paymentRequestDto.getCardNumber());
        validateCardStatus(card);

        Long amount = paymentRequestDto.getAmount();

        Long balanceSnapshot = card.getBalance();

        simulateProcessingDelay();

        if (balanceSnapshot < amount) {
            throw new ApplicationException(CommonErrors.INSUFFICIENT_BALANCE);
        }

        Long newBalance = balanceSnapshot - amount;
        card.setBalance(newBalance);
        cardRepository.save(card);

        decreaseAccountCommonBalanceUnsafe(card.getAccountEntity(), amount);

        TransactionEntity transaction = saveTransaction(card, amount, TransactionStatus.SUCCESS);

        log.info("Unsafe Payment Ended");
        return buildResponse(transaction, newBalance);
    }

    @Override
    @Transactional
    public PaymentResponseDto paySafe(PaymentRequestDto paymentRequestDto) {
        log.info("Safe Payment Started...");

        CardEntity card = cardRepository.findByCardNumberForUpdate(paymentRequestDto.getCardNumber())
                .orElseThrow(() -> new ApplicationException(CommonErrors.CARD_NOT_FOUND));
        validateCardStatus(card);

        Long amount = paymentRequestDto.getAmount();

        if (card.getBalance() < amount) {
            throw new ApplicationException(CommonErrors.INSUFFICIENT_BALANCE);
        }

        Long newBalance = card.getBalance() - amount;
        card.setBalance(newBalance);
        cardRepository.save(card);

        decreaseAccountCommonBalanceSafe(card.getAccountEntity(), amount);

        TransactionEntity transaction = saveTransaction(card, amount, TransactionStatus.SUCCESS);

        log.info("Safe Payment Ended");
        return buildResponse(transaction, newBalance);
    }

    private CardEntity getCardByNumber(Long cardNumber) {
        return cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ApplicationException(CommonErrors.CARD_NOT_FOUND));
    }

    private void validateCardStatus(CardEntity card) {
        if (Status.BLOCKED.equals(card.getStatus())) {
            throw new ApplicationException(CommonErrors.CARD_BLOCKED);
        }
    }

    private void decreaseAccountCommonBalanceUnsafe(AccountEntity account, Long amount) {
        account.setCommonBalance(account.getCommonBalance() - amount);
        accountRepository.save(account);
    }

    private void decreaseAccountCommonBalanceSafe(AccountEntity account, Long amount) {
        AccountEntity lockedAccount = accountRepository.findByIdForUpdate(account.getId())
                .orElseThrow(() -> new ApplicationException(CommonErrors.ACCOUNT_NOT_FOUND));
        lockedAccount.setCommonBalance(lockedAccount.getCommonBalance() - amount);
        accountRepository.save(lockedAccount);
    }

    private TransactionEntity saveTransaction(CardEntity card, Long amount, TransactionStatus status) {
        TransactionEntity transaction = TransactionEntity.builder()
                .transactionNumber(UUID.randomUUID())
                .sourceCardNumber(Long.valueOf(card.getCardNumber()))
                .destinationCardNumber(null)
                .amount(amount)
                .paymentType(PaymentType.WITHDRAW)
                .currency(card.getAccountEntity() != null ? card.getAccountEntity().getCurrency() : null)
                .transactionStatus(status)
                .cardEntity(card)
                .build();
        return transactionRepository.save(transaction);
    }

    private PaymentResponseDto buildResponse(TransactionEntity transaction, Long balanceAfter) {
        return PaymentResponseDto.builder()
                .transactionNumber(transaction.getTransactionNumber())
                .transactionStatus(transaction.getTransactionStatus())
                .cardNumber(transaction.getSourceCardNumber() != null
                        ? transaction.getSourceCardNumber().intValue()
                        : null)
                .balanceAfter(balanceAfter)
                .build();
    }

    private void simulateProcessingDelay() {
        try {
            Thread.sleep(RACE_WINDOW_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
