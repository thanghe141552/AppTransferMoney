//package com.example.kafka.consumer;
//
//import com.example.entity.Transaction;
//import com.example.entity.request.CreateTransactionRequest;
//import com.example.service.TransactionService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.annotation.RetryableTopic;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.retry.annotation.Backoff;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.event.TransactionPhase;
//import org.springframework.transaction.event.TransactionalEventListener;
//
//@Component
//public class TransferEventConsumer {
//
//    private final TransactionService transactionService;
//
//    public TransferEventConsumer(@Qualifier("transactionServiceImpl") TransactionService transactionService) {
//        this.transactionService = transactionService;
//    }
//
//    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 1000, multiplier = 2))
//    @KafkaListener(topics = "transaction-transfer-topic", groupId = "transaction-transfer-group", containerFactory = "kafkaListenerContainerFactory")
//    public void handleTransferEvent(@Payload CreateTransactionRequest createTransactionRequest,
//                                    Acknowledgment ack) {
//        try {
//            System.out.println("received " + createTransactionRequest.toString());
////            transactionService.createSingleTransactionWithKafka(createTransactionRequest);
//            ack.acknowledge();
//        } catch (Exception e) {
//            // Log or retry
//        }
//    }
//}