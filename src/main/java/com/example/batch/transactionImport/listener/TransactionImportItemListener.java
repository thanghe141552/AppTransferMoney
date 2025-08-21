//package com.example.batch.transactionImport.listener;
//
//import com.example.constant.AppConstant;
//import com.example.constant.MessageConstant;
//import com.example.entity.Transaction;
//import com.example.entity.request.CreateTransactionRequest;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.NonNull;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.ItemProcessListener;
//import org.springframework.batch.core.ItemReadListener;
//import org.springframework.batch.core.ItemWriteListener;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.listener.ItemListenerSupport;
//import org.springframework.batch.core.scope.context.StepSynchronizationManager;
//import org.springframework.batch.item.Chunk;
//import org.springframework.batch.item.ExecutionContext;
//import org.springframework.batch.item.file.FlatFileParseException;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.UUID;
//
//@Component
//@Slf4j
//public class TransactionImportItemListener extends ItemListenerSupport<CreateTransactionRequest, Transaction> {
//
//    public ItemProcessListener<? super CreateTransactionRequest, ? super Transaction> asItemProcessListener() {
//        return this;
//    }
//
//    public ItemReadListener<? super CreateTransactionRequest> asItemReaderListener() {
//        return this;
//    }
//
//    public ItemWriteListener<? super Transaction> asItemWriterListener() {
//        return this;
//    }
//
//    @Override
//    public void afterWrite(@NonNull Chunk<? extends Transaction> items) {
//
//        log.info("[START] After write with chunk : {}", items.getItems());
//
//        List<String> transactonList = getTransactionListFromContext();
//        long successTransaction = getNumberTransactionSuccessFromContext();
//
//        for (Transaction transaction : items) {
//
////            TransactionResponse transactionResponse  = new TransactionResponse(String.valueOf(transaction.getTransactionId()),
////                    String.valueOf(transaction.getFromAccount().getAccountId()), String.valueOf(transaction.getToAccount().getAccountId()),
////                    String.valueOf(transaction.getAmount()), "");
//
//
//            transactonList.add("Record: {" + transaction.getFromAccount().getId() + "," + transaction.getToAccount().getId() + "," + transaction.getAmount() + "} added successfully");
//            successTransaction++;
//        }
//
//        setTransactionListToContext(transactonList);
//        setNumberTransactionSuccessToContext(successTransaction);
//
//        log.info("[END] After write with chunk : {}", items.getItems());
//
//    }
//
//    @Override
//    public void onProcessError(CreateTransactionRequest item, Exception e) {
////        TransactionResponse transactionResponse = new TransactionResponse(String.valueOf(UUID.randomUUID()),
////                item.getFromAccountId(), item.getToAccountId(), item.getAmount(), e.getMessage());
//
//        log.info("[START] Error process with transaction : {}", UUID.randomUUID());
//
//        List<String> transactonList = getTransactionListFromContext();
//        long failTransaction = getNumberTransactionFailFromContext();
//
//        transactonList.add("Record: {" + item.getFromAccountId() + "," + item.getToAccountId() + "," + item.getAmount() + "} added fail with message " + e.getMessage());
//        failTransaction++;
//
//        setTransactionListToContext(transactonList);
//        setNumberTransactionFailToContext(failTransaction);
//
//        log.info("[END] Error process with transaction : {}", UUID.randomUUID());
//
//    }
//
//    @Override
//    public void onReadError(@NonNull Exception e) {
//        if (e instanceof FlatFileParseException ex) {
//            String line = ex.getInput();
//
//            log.info("[START] Error read with line : {}", line);
//
////            TransactionFileErrResponse transactionFileErrResponse = new TransactionFileErrResponse(UUID.randomUUID().toString(),
////                    line, MessageConstant.INVALID_FILE_LINE_FORMAT);
//            List<String> transactonList = getTransactionListFromContext();
//            long failTransaction = getNumberTransactionFailFromContext();
//
//            transactonList.add("Record: {" + line + "} added fail with message " + MessageConstant.INVALID_FILE_LINE_FORMAT);
//            failTransaction++;
//
//            setTransactionListToContext(transactonList);
//            setNumberTransactionFailToContext(failTransaction);
//
//            log.info("[END] Error read with line : {}", line);
//
//        }
//    }
//
//    @Override
//    public void onWriteError(@NonNull Exception exception, @NonNull Chunk<? extends Transaction> items) {
//
//        log.info("[START] Error write with chunk : {}", items.getItems());
//
//        List<String> transactonList = getTransactionListFromContext();
//        long failTransaction = getNumberTransactionFailFromContext();
//
//        for (Transaction transaction : items) {
////            TransactionResponse transactionResponseError  = new TransactionResponse(String.valueOf(transaction.getTransactionId()),
////                    String.valueOf(transaction.getFromAccount().getAccountId()), String.valueOf(transaction.getToAccount().getAccountId()),
////                    String.valueOf(transaction.getAmount()), exception.getMessage());
//
//            transactonList.add("Record: {" + transaction.getFromAccount().getId() + "," + transaction.getToAccount().getId() + "," + transaction.getAmount() + "} added fail with message " + exception.getMessage());
//            failTransaction++;
//        }
//
//        setTransactionListToContext(transactonList);
//        setNumberTransactionFailToContext(failTransaction);
//
//        log.info("[END] Error write with chunk : {}", items.getItems());
//
//    }
//
//    private ExecutionContext getJobExecutionContext() {
//        JobExecution jobExecution = Objects.requireNonNull(StepSynchronizationManager.getContext()).getStepExecution().getJobExecution();
//        return jobExecution.getExecutionContext();
//    }
//
//    private List<String> getTransactionListFromContext() {
//        ExecutionContext context = getJobExecutionContext();
//        ObjectMapper mapper = new ObjectMapper();
//        return context.get(AppConstant.TRANSACTION_LIST) == null ? new ArrayList<>() :
//                mapper.convertValue(context.get(AppConstant.TRANSACTION_LIST), new TypeReference<>() {
//                });
//    }
//
//    private long getNumberTransactionSuccessFromContext() {
//        ExecutionContext context = getJobExecutionContext();
//        return context.get(AppConstant.SUCCESS_TRANSACTION) == null ? 0L : Long.parseLong(String.valueOf(context.get(AppConstant.SUCCESS_TRANSACTION)));
//    }
//
//    private long getNumberTransactionFailFromContext() {
//        ExecutionContext context = getJobExecutionContext();
//        return context.get(AppConstant.ERROR_TRANSACTION) == null ? 0L : Long.parseLong(String.valueOf(context.get(AppConstant.ERROR_TRANSACTION)));
//    }
//
//    private void setTransactionListToContext(List<String> list) {
//        ExecutionContext context = getJobExecutionContext();
//        context.put(AppConstant.TRANSACTION_LIST, list);
//    }
//
//    private void setNumberTransactionSuccessToContext(long numberTransaction) {
//        ExecutionContext context = getJobExecutionContext();
//        context.put(AppConstant.SUCCESS_TRANSACTION, numberTransaction);
//    }
//
//    private void setNumberTransactionFailToContext(long numberTransaction) {
//        ExecutionContext context = getJobExecutionContext();
//        context.put(AppConstant.ERROR_TRANSACTION, numberTransaction);
//    }
//
//}
