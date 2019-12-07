package payment.api.mapper;

import payment.api.model.CreateTransactionRequest;
import payment.api.model.TransactionDto;

public class TransactionMapper {
    public static TransactionDto toTransactionDto(CreateTransactionRequest transactionRequest) {
        return new TransactionDto(
                transactionRequest.getUserUid(),
                transactionRequest.getProviderUid(),
                transactionRequest.getAmount(),
                transactionRequest.getCurrency(),
                transactionRequest.getStatus(),
                transactionRequest.getExtendedStatus(),
                transactionRequest.getDate(),
                transactionRequest.getComment(),
                transactionRequest.getSourceUid(),
                transactionRequest.getSourceName()
        );
    }
}
