//package com.example.batch.transactionImport.storage;
//
//import com.example.entity.response.TransactionFileErrResponse;
//import com.example.entity.response.TransactionResponse;
//import lombok.Data;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//
//@Component
//@Scope("job")
//@Lazy
//@Data
//public class TransactionJobStorage {
//    private final Map<String, Object> transactonMap = new LinkedHashMap<>();
//    private long successTransaction = 0;
//    private long errorTransaction = 0;
//
//    public void addTransaction(TransactionResponse transactionResponse) {
//        transactonMap.put(transactionResponse.getTransactionId(), transactionResponse);
//    }
//
//    public void addFileErrTransaction(TransactionFileErrResponse transactionFileErrResponse) {
//        transactonMap.put(transactionFileErrResponse.getTransactionId(), transactionFileErrResponse);
//        errorTransaction++;
//    }
//
//    public void removeTransaction(String transactionResponseId) {
//        transactonMap.remove(transactionResponseId);
//    }
//
//    public void replaceTransaction(TransactionResponse transactionResponse) {
//        transactonMap.replace(transactionResponse.getTransactionId(), transactionResponse);
//    }
//
//    public List<Object> getAllTransaction() {
//        return transactonMap.values().stream().toList();
//    }
//
//}
