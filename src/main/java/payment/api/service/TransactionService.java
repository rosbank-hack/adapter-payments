package payment.api.service;

import payment.api.model.CreateTransactionRequest;

import javax.annotation.Nonnull;

public interface TransactionService {
    void createTransaction(@Nonnull CreateTransactionRequest request);
}
