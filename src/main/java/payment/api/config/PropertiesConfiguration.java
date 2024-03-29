package payment.api.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import payment.api.config.properties.KafkaProperties;

@Configuration
@EnableConfigurationProperties({
        KafkaProperties.class
})
public class PropertiesConfiguration { }
