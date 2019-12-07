package payment.api.service;

import com.github.voteva.Operation;

public interface KafkaProducerService {

    void send(String topic, Operation operation);
}
