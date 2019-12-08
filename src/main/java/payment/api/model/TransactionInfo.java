package payment.api.model;

import lombok.Data;
import payment.api.enums.TransactionStatus;
import payment.api.enums.TransactionExtendedStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class TransactionInfo {
    private UUID userUid;
    private UUID providerUid;
    private BigDecimal amount;
    private String currency;
    private TransactionStatus status;
    private TransactionExtendedStatus extendedStatus;
    private Date date;
    private String comment;
    private UUID sourceUid;
    private String sourceName;
}
