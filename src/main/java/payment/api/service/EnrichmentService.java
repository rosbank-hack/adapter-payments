package payment.api.service;

import payment.api.model.TransactionDto;

import javax.annotation.Nonnull;

public interface EnrichmentService {

    void enrichTransaction(@Nonnull TransactionDto transaction);
}
