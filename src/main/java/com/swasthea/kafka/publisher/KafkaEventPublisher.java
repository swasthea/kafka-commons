package com.swasthea.kafka.publisher;

import com.swasthea.kafka.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Spring bean that publishes {@link DomainEvent} instances to a configured Kafka topic.
 *
 * <p>Registered automatically by {@code SwastheaKafkaAutoConfiguration} when
 * {@code spring.kafka.bootstrap-servers} is present in the application properties.
 *
 * <p>Failures are <em>logged but not re-thrown</em> — the caller (e.g. a service method)
 * is never blocked by a Kafka outage.
 */
public class KafkaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate, String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    /**
     * Publishes a domain event to the shared Kafka topic.
     * Uses {@code eventId} as the partition key for ordering within the same logical stream.
     *
     * @param event the event to publish; must not be {@code null}
     */
    public <T> void publish(DomainEvent<T> event) {
        try {
            kafkaTemplate.send(topic, event.getEventId(), event);
            log.info("[Kafka] Published {} eventId={} source={} topic={}",
                    event.getEventType(), event.getEventId(), event.getSource(), topic);
        } catch (Exception ex) {
            log.error("[Kafka] Failed to publish {} eventId={}: {}",
                    event.getEventType(), event.getEventId(), ex.getMessage(), ex);
        }
    }
}
