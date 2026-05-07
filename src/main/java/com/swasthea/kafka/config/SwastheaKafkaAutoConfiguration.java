package com.swasthea.kafka.config;

import com.swasthea.kafka.publisher.KafkaEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Spring Boot auto-configuration for the Swasthea Kafka publisher.
 *
 * <p>Activates only when:
 * <ul>
 *   <li>{@code spring-kafka} is on the classpath</li>
 *   <li>{@code spring.kafka.bootstrap-servers} is set in application properties</li>
 * </ul>
 *
 * <p>The {@link KafkaTemplate} itself is provided by Spring Boot's own Kafka
 * auto-configuration — this class just wraps it in {@link KafkaEventPublisher}.
 * SSL and serialisation are controlled entirely via {@code spring.kafka.*} properties.
 */
@AutoConfiguration
@ConditionalOnClass(KafkaTemplate.class)
@ConditionalOnProperty(prefix = "spring.kafka", name = "bootstrap-servers")
public class SwastheaKafkaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KafkaEventPublisher kafkaEventPublisher(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${swasthea.kafka.topic:swasthea-dev}") String topic) {
        return new KafkaEventPublisher(kafkaTemplate, topic);
    }
}
