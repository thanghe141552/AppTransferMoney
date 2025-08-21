//package com.example.kafka.config;
//
//import com.example.entity.request.CreateTransactionRequest;
//import io.micrometer.core.instrument.ImmutableTag;
//import io.micrometer.core.instrument.MeterRegistry;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.MicrometerConsumerListener;
//import org.springframework.kafka.listener.ContainerProperties;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//
//import java.text.MessageFormat;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Objects;
//
//@Configuration
//public class ConsumerConfiguration {
//
//    private static final Logger logger = LoggerFactory.getLogger(ConsumerConfiguration.class);
//
//    private final Environment env;
//
//    public ConsumerConfiguration(Environment env) {
//        this.env = env;
//    }
//
//    /**
//     * kafka with no custom event
//     */
//    @Bean(name = "consumerFactoryNoEvent")
//    public ConsumerFactory<String, String> consumerFactory1() {
//        HashMap<String, Object> props = new HashMap<>();
//        String bootstrapServers = env.getProperty("api.callApi.kafka.bootstrapServers");
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.CLIENT_ID_CONFIG, env.getProperty("api.callApi.kafka.kk_clientId"));
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, env.getProperty("api.callApi.kafka.groupId"));
//
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
//        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000"); // 10
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
//        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
//        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "300000"); // 5
//        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "20971520"); // 20M
//        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "20971520"); // 20M
//        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000"); // 20M
//
//        String sercu = env.getProperty("api.callApi.kafka.kk_clientId");
//        if (Objects.requireNonNull(sercu).equals("Y")) {
//            props.put("acks", "all");
//            props.put("security.protocol", "SASL_PLAINTEXT");
//            props.put("sasl.mechanism", "PLAIN");
//            props.put("sasl.jaas.config",
//                    formatMessage(
//                            "org.apache.kafka.common.security.plain.PlainLoginModule required username={0}  password={1};",
//                            new String[]{env.getProperty("api.callApi.kafka.kk_user_name"), env.getProperty("api.callApi.kafka.kk_pw")}));
//
//        }
//
//        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new StringDeserializer());
//    }
//
//    @Bean(name = "kafkaListenerContainerFactoryNoEvent")
//    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory1() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory1());
//        factory.setRecordFilterStrategy(consumerRecord -> {
//            logger.info("Recived message from topic : " + consumerRecord.topic() + " with data : " + consumerRecord.value());
//            return false;
//        });
//        return factory;
//    }
//
//    /**
//     * kafka with custom event
//     */
//    @Bean(name = "consumerFactory")
//    public ConsumerFactory<String, CreateTransactionRequest> consumerFactory(MeterRegistry meterRegistry) {
//        HashMap<String, Object> props = new HashMap<>();
//        String bootstrapServers = env.getProperty("api.callApi.kafka.bootstrapServers");
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.CLIENT_ID_CONFIG, env.getProperty("api.callApi.kafka.kk_clientId"));
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, env.getProperty("api.callApi.kafka.groupId"));
////        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
//        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000"); // 10
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
//        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
//        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "300000"); // 5
//        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "20971520"); // 20M
//        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "20971520"); // 20M
//        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000"); // 20M
//
//        DefaultKafkaConsumerFactory<String, CreateTransactionRequest> factory = new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(CreateTransactionRequest.class));
//
//        factory.addListener(new MicrometerConsumerListener<>(meterRegistry,
//                Collections.singletonList(new ImmutableTag("testMeasure", "testKafkaMeasure"))));
//
//        return factory;
//    }
//
//    @Bean(name = "kafkaListenerContainerFactory")
//    public ConcurrentKafkaListenerContainerFactory<String, CreateTransactionRequest> kafkaListenerContainerFactory(MeterRegistry meterRegistry) {
//        ConcurrentKafkaListenerContainerFactory<String, CreateTransactionRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory(meterRegistry));
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
//        factory.setRecordFilterStrategy(consumerRecord -> {
//            logger.info("Recived message from topic : " + consumerRecord.topic() + " with data : " + consumerRecord.value());
//            return false;
//        });
//
//        return factory;
//    }
//
//    public String formatMessage(String msgTemplate, String[] params) {
//        if (params != null) {
//            MessageFormat formater = new MessageFormat(msgTemplate);
//            return formater.format(params);
//        }
//        return msgTemplate;
//    }
//}