//package com.example.batch.transactionImport.listener;
//
//import com.example.batch.transactionImport.storage.TransactionJobStorage;
//import org.springframework.batch.core.ExitStatus;
//import org.springframework.batch.core.StepExecution;
//import org.springframework.batch.core.StepExecutionListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TransactionImportStepListener implements StepExecutionListener {
//
//    @Autowired
//    TransactionJobStorage transactionJobStorage;
//
//    @Override
//    public ExitStatus afterStep(StepExecution stepExecution) {
//        transactionJobStorage.setSuccessTransaction(stepExecution.getWriteCount());
//        transactionJobStorage.setErrorTransaction(stepExecution.getSkipCount());
//        return ExitStatus.COMPLETED;
//    }
//}