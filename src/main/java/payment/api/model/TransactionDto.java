package payment.api.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class TransactionDto {
    private final UUID userUid;
    private final UUID providerUid;
    private final BigDecimal amount;
    private final String currency;
    private final TransactionExtendedStatus status;
    private final Date date;
    private final String comment;
    private final UUID sourceUid;
    private final String sourceName;
}
