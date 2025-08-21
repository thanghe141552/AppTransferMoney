//package com.example.kafka.producer;
//
//import com.example.entity.Transaction;
//import com.example.entity.request.CreateTransactionRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.stream.Collectors;
//
//@Component
//public class TransferEventProducer {
//    private final KafkaTemplate<String, CreateTransactionRequest> kafkaTemplate;
//
//    public TransferEventProducer(@Qualifier("kafkaTransactionTemplate") KafkaTemplate<String, CreateTransactionRequest> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void publishTransfer(CreateTransactionRequest createTransactionRequest) {
//        kafkaTemplate.send("transaction-transfer-topic", createTransactionRequest.getToAccountId(), createTransactionRequest);
//    }
//
//    public CompletableFuture<Long> publishMultipleTransferVirtualThread(List<CreateTransactionRequest> createTransactionRequests) {
//        System.out.println(createTransactionRequests.size());
//        List<CompletableFuture<SendResult<String, CreateTransactionRequest>>> futures;
//        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
//            futures = createTransactionRequests.stream()
//                    .map(createTransactionRequest -> CompletableFuture.supplyAsync(()
//                            -> {
//                        try {
//                            return kafkaTemplate.send("transaction-transfer-topic", createTransactionRequest.getToAccountId(), createTransactionRequest).get();
//                        } catch (InterruptedException | ExecutionException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }, executorService))
//                    .toList();
//        }
//        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApply( v -> futures.stream().mapToLong(future -> future.isCompletedExceptionally() ? 0 : 1).sum());
//    }
//}
