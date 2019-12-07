package payment.api.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        KafkaProperties.class,
        ApplicationProperties.class
})
public class PropertiesConfiguration {
}
