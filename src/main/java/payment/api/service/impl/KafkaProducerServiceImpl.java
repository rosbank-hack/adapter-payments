package payment.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.voteva.Operation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import payment.api.service.KafkaProducerService;

import javax.annotation.Nonnull;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl
        implements KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public void send(@Nonnull String topic, @Nonnull Operation operation) {
        final String data = objectMapper.writeValueAsString(operation);

        if (log.isDebugEnabled()) {
            log.debug(data);
        }
        kafkaTemplate.send(topic, data);
    }
}
