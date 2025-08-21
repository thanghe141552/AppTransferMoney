//package com.example.batch.transactionImport.listener;
//
//import com.example.batch.transactionImport.storage.TransactionJobStorage;
//import com.example.constant.MessageConstant;
//import com.example.entity.Transaction;
//import com.example.entity.request.TransactionRequest;
//import com.example.entity.response.TransactionFileErrResponse;
//import com.example.entity.response.TransactionResponse;
//import lombok.NonNull;
//import org.springframework.batch.core.SkipListener;
//import org.springframework.batch.item.file.FlatFileParseException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//@Component
//public class TransactionImportSkipListener implements SkipListener<TransactionRequest, Transaction> {
//
//    @Autowired
//    private TransactionJobStorage transactionJobStorage;
//
//    @Override
//    public void onSkipInRead(@NonNull Throwable t) {
//        if (t instanceof FlatFileParseException ex) {
//            String line = ex.getInput();
//            transactionJobStorage.addFileErrTransaction(new TransactionFileErrResponse(UUID.randomUUID().toString(), line, MessageConstant.INVALID_FILE_LINE_FORMAT));
//        }
//        SkipListener.super.onSkipInRead(t);
//    }
//
//    @Override
//    public void onSkipInProcess(@NonNull TransactionRequest item, @NonNull Throwable t) {
//        TransactionResponse transactionResponse = new TransactionResponse(UUID.randomUUID().toString(), item.getFromAccountId(),
//                item.getToAccountId(), item.getAmount(), t.getMessage());
//        transactionJobStorage.addTransaction(transactionResponse);
//        SkipListener.super.onSkipInProcess(item, t);
//    }
//
//    @Override
//    public void onSkipInWrite(@NonNull Transaction item, @NonNull Throwable t) {
//        transactionJobStorage.replaceTransaction(new TransactionResponse(item.getTransactionId().toString(),
//                item.getFromAccount().getAccountId().toString(), item.getToAccount().getAccountId().toString(),
//                item.getAmount().toString(), t.getMessage()));
//        SkipListener.super.onSkipInWrite(item, t);
//    }
//
//}
