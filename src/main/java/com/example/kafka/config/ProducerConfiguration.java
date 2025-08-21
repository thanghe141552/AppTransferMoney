//package com.example.kafka.config;
//
//import com.example.entity.request.CreateTransactionRequest;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//
//import java.text.MessageFormat;
//import java.util.HashMap;
//import java.util.Objects;
//
//@Configuration
//public class ProducerConfiguration {
//
//    private final Environment env;
//
//    public ProducerConfiguration(Environment env) {
//        this.env = env;
//    }
//
//    /**
//     * kafka producer with no custom event
//     */
//    @Bean(name = "producerFactory")
//    public ProducerFactory<String, String> producerFactory() {
//        HashMap<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("api.callApi.kafka.bootstrapServers"));
//        props.put(ProducerConfig.CLIENT_ID_CONFIG, env.getProperty("api.callApi.kafka.kk_clientId"));
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "20971520");
//
//        String sercu = env.getProperty("api.callApi.kafka.kk_clientId");
//        if (Objects.requireNonNull(sercu).equals("Y")) {
//            props.put("acks", "all");
//            props.put("security.protocol", "SASL_PLAINTEXT");
//            props.put("sasl.mechanism", "PLAIN");
//            props.put("sasl.jaas.config", formatMessage(
//                    "org.apache.kafka.common.security.plain.PlainLoginModule required username={0}  password={1};",
//                    new String[]{env.getProperty("api.callApi.kafka.kk_user_name"), env.getProperty("api.callApi.kafka.kk_pw")}));
//
//        }
//        return new DefaultKafkaProducerFactory<>(props);
//    }
//
//    /**
//     * kafka producer with custom event
//     */
//    @Bean(name = "producerCreateTransaction")
//    public ProducerFactory<String, CreateTransactionRequest> producerFactory1() {
//        HashMap<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("api.callApi.kafka.bootstrapServers"));
////        props.put(ProducerConfig.CLIENT_ID_CONFIG, env.getProperty("api.callApi.kafka.kk_clientId"));
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
//        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "20971520");
//        return new DefaultKafkaProducerFactory<>(props);
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