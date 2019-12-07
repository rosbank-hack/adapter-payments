package payment.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import payment.api.mapper.OperationMapper;
import payment.api.service.EnrichmentService;
import payment.api.config.KafkaProperties;
import payment.api.config.PropertiesConfiguration;
import payment.api.model.TransactionDto;
import payment.api.service.KafkaProducerService;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static payment.api.consts.Constants.BONUS_SERVICE_NAME;
import static payment.api.consts.Constants.PROVIDER_SERVICE_NAME;
import static payment.api.consts.Constants.SERVICE_NAME;

@Service
@RequiredArgsConstructor
@ConditionalOnBean(PropertiesConfiguration.class)
public class EnrichmentServiceImpl implements EnrichmentService {

    private final KafkaProducerService kafkaProducerService;
    private final KafkaProperties kafkaProperties;

    @Override
    public void enrichTransaction(@Nonnull TransactionDto transaction) {
        String operationId = UUID.randomUUID().toString();

        //to events
        sendOperationToKafka(
                kafkaProperties.getEventTopic(),
                operationId,
                null,
                null,
                transaction
        );

        //to provider

        sendOperationToKafka(
                kafkaProperties.getProviderTopic(),
                operationId,
                PROVIDER_SERVICE_NAME,
                Collections.singletonMap("providerId", transaction.getProviderUid().toString()),
                transaction
        );

        //to bonus
        Map<String, String> bonusRequestData = new HashMap<>();
        bonusRequestData.put("providerId", transaction.getProviderUid().toString());
        bonusRequestData.put("userId", transaction.getUserUid().toString());
        bonusRequestData.put("amount", transaction.getAmount().toString());

        sendOperationToKafka(
                kafkaProperties.getBonusTopic(),
                operationId,
                BONUS_SERVICE_NAME,
                bonusRequestData,
                transaction
        );
    }

    private void sendOperationToKafka(
            String topic,
            String operationId,
            String requestedService,
            Map<String, String> requestData,
            TransactionDto transaction) {
        List<String> requiredServices = new ArrayList<>();
        requiredServices.add(PROVIDER_SERVICE_NAME);
        requiredServices.add(BONUS_SERVICE_NAME);

        kafkaProducerService.send(
                topic,
                OperationMapper.toOperation(
                        SERVICE_NAME,
                        SERVICE_NAME,
                        operationId,
                        requestedService,
                        requestData,
                        requiredServices,
                        transaction
                ));
    }
}
