//package com.example.batch.transactionImport.listener;
//
//import com.example.constant.AppConstant;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.batch.core.StepExecution;
//import org.springframework.batch.core.StepExecutionListener;
//import org.springframework.batch.item.ExecutionContext;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Iterator;
//import java.util.List;
//
//@Component("transactionReportReader")
//public class TransactionReportReader implements ItemReader<String>, StepExecutionListener {
//
//    private Iterator<String> dataIterator = Collections.emptyIterator();
//
//    @Override
//    public void beforeStep(StepExecution stepExecution) {
//        ExecutionContext context = stepExecution.getJobExecution().getExecutionContext();
//        ObjectMapper mapper = new ObjectMapper();
//        List<String> transactionList = context.get(AppConstant.TRANSACTION_LIST) == null ? new ArrayList<>() :
//                mapper.convertValue(context.get(AppConstant.TRANSACTION_LIST), new TypeReference<>() {
//                });
//
////        List<String> convertReport = new ArrayList<>();
////        convertReport = transactionMap.values().stream().map(o -> {
////            if(o instanceof TransactionResponse response){
////                if(!response.getFailMessage().isEmpty()){
////                    return "Record: " + response.getFromAccountId() + "," + response.getToAccountId() + "," + response.getAmount() + " added fail with message " + response.getFailMessage();
////                }
////                return "Record: " + response.getFromAccountId() + "," + response.getToAccountId() + "," + response.getAmount() + " added successfully";
////            }
////            if(o instanceof TransactionFileErrResponse response){
////                if(!response.getFailMessage().isEmpty()){
////                    return "Record: " + response.getErrorLine() + " added fail with message " + response.getFailMessage();
////                }
////                return "Record: " + response.getErrorLine() + " added successfully";
////            }
////            return o.toString();
////        }).toList();
//
//        this.dataIterator = transactionList.iterator();
//    }
//
//    @Override
//    public String read() {
//        return dataIterator.hasNext() ? dataIterator.next() : null;
//    }
//}