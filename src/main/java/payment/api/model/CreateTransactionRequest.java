package payment.api.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class CreateTransactionRequest {
    private UUID userUid;
    private UUID providerUid;
    private BigDecimal amount;
    private String currency;
    private TransactionExtendedStatus status;
    private Date date;
    private String comment;
    private UUID sourceUid;
    private String sourceName;
}
