//package com.example.batch.transactionImport.listener;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.JobExecutionListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class JobNotificationListener implements JobExecutionListener {
//
//    @Override
//    public void afterJob(JobExecution jobExecution) {
//        log.info("\uD83D\uDD04 JobListener - Sau khi thực thi: {} time end: {} status: {}", jobExecution.getJobId(), jobExecution.getEndTime(), jobExecution.getStatus());
//    }
//
//    @Override
//    public void beforeJob(JobExecution jobExecution) {
//        log.info("\uD83D\uDD04 JobListener - Trước khi thực thi: {} time start: {}", jobExecution.getJobId(), jobExecution.getStartTime());
//    }
//}