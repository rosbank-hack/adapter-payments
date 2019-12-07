package payment.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Validated
@ConfigurationProperties("application")
public class ApplicationProperties {

    @NotBlank
    private String paymentServiceName;

    @NotNull
    private List<String> requiredServices;
}
