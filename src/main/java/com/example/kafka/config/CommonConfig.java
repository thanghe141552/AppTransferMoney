//package com.example.kafka.config;
//
//import com.example.kafka.constant.KafkaConstant;
//import org.apache.kafka.clients.CommonClientConfigs;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.kafka.core.KafkaAdmin;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class CommonConfig {
//
//    private final Environment env;
//
//    public CommonConfig(Environment env) {
//        this.env = env;
//    }
//
//    @Bean
//    public KafkaAdmin kafkaAdmin() {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("api.callApi.kafka.bootstrapServers"));
//        return new KafkaAdmin(configs);
//    }
//}
