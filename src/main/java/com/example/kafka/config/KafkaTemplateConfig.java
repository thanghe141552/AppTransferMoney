//package com.example.kafka.config;
//
//import com.example.entity.Transaction;
//import com.example.entity.request.CreateTransactionRequest;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.KafkaAdmin;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Configuration
//public class KafkaTemplateConfig {
//    @Bean
//    public KafkaTemplate<String, CreateTransactionRequest> kafkaTransactionTemplate(@Qualifier("producerCreateTransaction") ProducerFactory<String, CreateTransactionRequest> producerFactory,
//                                                                                    @Qualifier("transactionTriggerTopic") NewTopic transactionTriggerTopic,
//                                                                                    KafkaAdmin kafkaAdmin) {
//        KafkaTemplate<String, CreateTransactionRequest> kafkaTemplate = new KafkaTemplate<>(producerFactory);
//        // Optionally configure default topic or producer listener
//        kafkaTemplate.setDefaultTopic(transactionTriggerTopic.name());
//        kafkaTemplate.setKafkaAdmin(kafkaAdmin);
//        return kafkaTemplate;
//    }
//
////    @Bean
////    public KafkaAdmin.NewTopics topicBuilder() {
////        return KafkaAdmin.NewTopicBuilder()
////                .defaultNumPartitions(3)  // Set default number of partitions
////                .defaultReplicationFactor((short) 1); // Set default replication factor
////    }
//
//}
