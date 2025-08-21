package com.example.controller;

import com.example.entity.Transaction;
import com.example.entity.projection.TransactionSummary;
import com.example.entity.request.CreateTransactionRequest;
import com.example.entity.request.FilterTransactionRequest;
import com.example.entity.response.CreateTransactionResponse;
import com.example.entity.response.FilterTransactionResponse;
import com.example.entity.response.TotalAmountTransactionResponse;
//import com.example.kafka.producer.TransferEventProducer;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
//    private final TransferEventProducer transferEventProducer;

    public TransactionController(@Qualifier("transactionServiceImpl") TransactionService transactionService
//            , TransferEventProducer transferEventProducer
    ) {
        this.transactionService = transactionService;
//        this.transferEventProducer = transferEventProducer;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN') or @accountSecurity.isAccountOwner(#createTransactionRequest.fromAccountId)")
    public ResponseEntity<CreateTransactionResponse> createTransaction(@RequestBody CreateTransactionRequest createTransactionRequest) {
        CreateTransactionResponse transactionResponse = transactionService.createSingleTransaction(createTransactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);
    }

    @PostMapping("/createMultiple")
    public ResponseEntity<List<CreateTransactionResponse>> createMultipleTransaction(@RequestBody List<CreateTransactionRequest> createTransactionRequests) {
        List<CreateTransactionResponse> createTransactionResponses = transactionService.createMultipleTransaction(createTransactionRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(createTransactionResponses);
    }

    @GetMapping("/getByDate")
    public ResponseEntity<List<FilterTransactionResponse>> getTransactionHistoryByCreatedTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createTime){
        List<FilterTransactionResponse> transactionResponses = transactionService.getTransactionHistoryByCreatedTime(createTime);
        return ResponseEntity.ok(transactionResponses);
    }

    @GetMapping("/getTotalAmountByDate")
    public ResponseEntity<TotalAmountTransactionResponse> getTotalAmountTransactionByTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time){
        TotalAmountTransactionResponse transactionResponses = transactionService.getTotalAmountTransactionByTime(time);
        return ResponseEntity.ok(transactionResponses);
    }

    @PostMapping("/search")
    public ResponseEntity<List<FilterTransactionResponse>> searchTransaction(@RequestBody FilterTransactionRequest filterTransactionRequest){
        List<FilterTransactionResponse> transactionResponses = transactionService.search(filterTransactionRequest);
        return ResponseEntity.ok(transactionResponses);
    }

//    @PostMapping("/kafka-create")
//    @PreAuthorize("hasRole('ADMIN') or @accountSecurity.isAccountOwner(#request.fromAccountId)")
//    public ResponseEntity<?> createTransactionByKafka(@RequestBody CreateTransactionRequest request) {
//        transferEventProducer.publishTransfer(request);
//        return ResponseEntity.accepted().body("Transfer event published");
//    }
//
//    @PostMapping("/kafka-create-multiple")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<?> createMultipleTransactionByKafka(@RequestBody List<CreateTransactionRequest> requests) {
//        transferEventProducer.publishMultipleTransferVirtualThread(requests);
//        return ResponseEntity.accepted().body("Transfer event published");
//    }
}
