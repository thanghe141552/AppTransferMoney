//package com.example.kafka.config;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;
//
//@Configuration
//public class KafkaTopicConfig {
//    @Bean(name = "transactionTriggerTopic" )
//    public NewTopic moneyTransferTopic() {
//        return TopicBuilder.name("transaction-transfer-topic").partitions(3).replicas(1).build();
//    }
//
//    @Bean(name = "accountNotificationTopic" )
//    public NewTopic notificationTopic() {
//        return TopicBuilder.name("account-notification-topic").partitions(3).replicas(1).build();
//    }
//
//    @Bean(name = "errorTopic" )
//    public NewTopic fraudDetectionTopic() {
//        return TopicBuilder.name("error-topic").partitions(3).replicas(1).build();
//    }
//
//}
