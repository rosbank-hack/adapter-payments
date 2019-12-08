package payment.api.service.impl;

import com.github.voteva.Operation;
import com.github.voteva.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import payment.api.config.PropertiesConfiguration;
import payment.api.config.properties.KafkaProperties;
import payment.api.model.CreateTransactionRequest;
import payment.api.service.TransactionService;
import payment.api.service.KafkaProducerService;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.Collections.singletonMap;
import static payment.api.constants.Services.BONUS_SERVICE_NAME;
import static payment.api.constants.Services.PROVIDER_SERVICE_NAME;
import static payment.api.constants.Services.SERVICE_NAME;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
@ConditionalOnBean(PropertiesConfiguration.class)
public class TransactionServiceImpl
        implements TransactionService {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private final KafkaProducerService kafkaProducerService;
    private final KafkaProperties kafkaProperties;

    @Override
    public void createTransaction(@Nonnull CreateTransactionRequest request) {
        final Operation operation = buildOperation(request);

        kafkaProducerService.send(kafkaProperties.getEventTopic(), operation);
        kafkaProducerService.send(kafkaProperties.getProviderTopic(), operation);
        kafkaProducerService.send(kafkaProperties.getBonusTopic(), operation);
    }

    @Nonnull
    private Operation buildOperation(@Nonnull CreateTransactionRequest request) {
        final Map<String, Service> services = newHashMap();
        services.put(SERVICE_NAME, new Service().setResponse(buildPaymentsResponse(request)));
        services.put(PROVIDER_SERVICE_NAME, new Service().setRequest(buildProviderRequest(request)));
        services.put(BONUS_SERVICE_NAME, new Service().setRequest(buildBonusRequest(request)));

        return new Operation()
                .setInitiator(SERVICE_NAME)
                .setPublisher(SERVICE_NAME)
                .setRequiredServices(newArrayList(SERVICE_NAME, PROVIDER_SERVICE_NAME, BONUS_SERVICE_NAME))
                .setOperationId(UUID.randomUUID().toString())
                .setUserId(request.getUserUid())
                .setServices(services);
    }

    @Nonnull
    private Map<String, String> buildProviderRequest(@Nonnull CreateTransactionRequest request) {
        return singletonMap("providerId", request.getProviderUid());
    }

    @Nonnull
    private Map<String, String> buildBonusRequest(@Nonnull CreateTransactionRequest request) {
        final Map<String, String> data = newHashMap();
        data.put("providerId", request.getProviderUid());
        data.put("userId", request.getUserUid());
        data.put("amount", request.getAmount().toString());
        return data;
    }

    @Nonnull
    private Map<String, String> buildPaymentsResponse(@Nonnull CreateTransactionRequest request) {
        final Map<String, String> response = newHashMap();
        response.put("userUid", request.getUserUid());
        response.put("providerUid", request.getProviderUid());
        response.put("currency", request.getCurrency());
        response.put("status", request.getStatus().name());
        response.put("extendedStatus", request.getExtendedStatus().name());
        response.put("date", new SimpleDateFormat(DATE_FORMAT).format(new Date()));
        response.put("amount", request.getAmount().toString());
        response.put("comment", request.getComment());
        response.put("sourceId", request.getSourceUid().toString());
        response.put("sourceName", request.getSourceName());
        return response;
    }
}
