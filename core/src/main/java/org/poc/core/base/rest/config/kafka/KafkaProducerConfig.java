/**
 * 
 */
package org.poc.core.base.rest.config.kafka;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/**
 * @author Sankha
 *
 */
public class KafkaProducerConfig {
	@Value(value = "${kafka.bootstrapAddress:localhost:9092}")
	private String bootstrapAddress;
	
	@Value(value="${kafka.topic.default:test}")
	private String defaultTopic;
	
	@Value(value="${kafka.timeout:5}")
	private String defaultTimeout;
	
	

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		KafkaTemplate<String, String> defaultKafkaTemplate = new KafkaTemplate<>(producerFactory());
		defaultKafkaTemplate.setDefaultTopic(defaultTopic);
		return defaultKafkaTemplate;
	}
	
	@Bean
	public AdminClient adminClient() {
		Properties properties = new Properties();
		properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapAddress);
		properties.put(AdminClientConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 10000);
		properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 2000);
		return AdminClient.create(properties);
//		 try (AdminClient client = AdminClient.create(properties)) {
//	            client.listTopics(new ListTopicsOptions().timeoutMs(ADMIN_CLIENT_TIMEOUT_MS)).listings().get();
//	        } catch (ExecutionException ex) {
//	            LOG.error("Kafka is not available, timed out after {} ms", ADMIN_CLIENT_TIMEOUT_MS);
//	            return;
//	        }
//		try (AdminClient client = AdminClient.create(properties))
//		{
//		    ListTopicsResult topics = client.listTopics();
//		    Set<String> names = topics.names().get();
//		    if (names.isEmpty())
//		    {
//		        // case: if no topic found.
//		    }
//		    return true;
//		}
//		catch (InterruptedException | ExecutionException e)
//		{
//		    // Kafka is not available
//		}
	}
}
