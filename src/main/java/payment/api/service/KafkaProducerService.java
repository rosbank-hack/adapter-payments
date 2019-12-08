package payment.api.service;

import com.github.voteva.Operation;

import javax.annotation.Nonnull;

public interface KafkaProducerService {
    void send(@Nonnull String topic, @Nonnull Operation operation);
}
