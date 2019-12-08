package payment.api.model;

import lombok.Data;
import payment.api.enums.TransactionExtendedStatus;
import payment.api.enums.TransactionStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CreateTransactionRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    private String userUid;
    @NotEmpty
    private String providerUid;
    @Min(0)
    @Max(1_000_000)
    @NotNull
    private BigDecimal amount;
    @NotEmpty
    private String currency;
    @NotNull
    private TransactionStatus status;
    @NotNull
    private TransactionExtendedStatus extendedStatus;
    @NotNull
    private String sourceUid;
    @NotNull
    private String sourceName;
}
