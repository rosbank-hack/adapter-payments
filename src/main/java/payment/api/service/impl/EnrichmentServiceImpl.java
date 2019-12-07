package payment.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import payment.api.mapper.OperationMapper;
import payment.api.service.EnrichmentService;
import payment.api.config.ApplicationProperties;
import payment.api.config.KafkaProperties;
import payment.api.config.PropertiesConfiguration;
import payment.api.model.TransactionDto;
import payment.api.service.KafkaProducerService;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnBean(PropertiesConfiguration.class)
public class EnrichmentServiceImpl implements EnrichmentService {

    private final KafkaProducerService kafkaProducerService;
    private final KafkaProperties kafkaProperties;
    private final ApplicationProperties applicationProperties;

    @Override
    public void enrichTransaction(@Nonnull TransactionDto transaction) {
        String operationId = UUID.randomUUID().toString();

        kafkaProducerService.send(
                kafkaProperties.getProviderTopic(),
                OperationMapper.toOperation(
                        applicationProperties.getPaymentServiceName(),
                        applicationProperties.getPaymentServiceName(),
                        operationId,
                        "provider",
                        Collections.singletonMap(
                                "providerId",
                                transaction.getProviderUid().toString()
                        ),
                        applicationProperties.getRequiredServices(),
                        transaction
                ));

        Map<String, String> bonusRequestData = new HashMap<>();
        bonusRequestData.put("provider", transaction.getProviderUid().toString());
        bonusRequestData.put("userId", transaction.getUserUid().toString());
        bonusRequestData.put("amount", transaction.getAmount().toString());

        kafkaProducerService.send(
                kafkaProperties.getBonusTopic(),
                OperationMapper.toOperation(
                        applicationProperties.getPaymentServiceName(),
                        applicationProperties.getPaymentServiceName(),
                        operationId,
                        "bonus",
                        bonusRequestData,
                        applicationProperties.getRequiredServices(),
                        transaction
                ));
    }
}
