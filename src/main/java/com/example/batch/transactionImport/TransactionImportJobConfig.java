//package com.example.batch.transactionImport;
//
//import com.example.batch.transactionImport.listener.JobNotificationListener;
//import com.example.batch.transactionImport.listener.TransactionImportItemListener;
//import com.example.builder.TransactionBuilder;
//import com.example.constant.AppConstant;
//import com.example.constant.MessageConstant;
//import com.example.entity.Account;
//import com.example.entity.Transaction;
//import com.example.entity.request.CreateTransactionRequest;
//import com.example.entity.request.CreateTransactionRequest;
//import com.example.exception.TransactionException;
//import com.example.repository.AccountRepository;
//import jakarta.persistence.EntityManagerFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.StepExecution;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.scope.context.StepSynchronizationManager;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.item.ExecutionContext;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.database.JpaItemWriter;
//import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.FlatFileItemWriter;
//import org.springframework.batch.item.file.LineMapper;
//import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
//import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.orm.jpa.JpaSystemException;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import java.math.BigDecimal;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.util.Objects;
//import java.util.UUID;
//
//@Configuration
//@Slf4j
//public class TransactionImportJobConfig {
//
//    @Bean(name = "transactionImportReader")
//    public FlatFileItemReader<CreateTransactionRequest> transactionFileReader(@Qualifier("lineMapperTransaction") LineMapper<CreateTransactionRequest> mapper) {
//        return new FlatFileItemReaderBuilder<CreateTransactionRequest>().name("TransactionImportReader").
//                resource(new FileSystemResource(AppConstant.FILE_TRANSACTION_INSERT_URL)).lineMapper(mapper).build();
//    }
//
//    @Bean(name = "lineMapperTransaction")
//    public LineMapper<CreateTransactionRequest> lineMapperTransaction(){
//        DefaultLineMapper<CreateTransactionRequest> lineMapper = new DefaultLineMapper<>();
//        lineMapper.setLineTokenizer(new DelimitedLineTokenizer(AppConstant.COMMA_DELIMITER));
//        lineMapper.setFieldSetMapper(fieldSet -> new CreateTransactionRequest(fieldSet.readString(0).trim(),
//                fieldSet.readString(1).trim(), fieldSet.readString(2).trim()));
//        return lineMapper;
//    }
//
//    @Bean(name = "transactionImportProcessor")
//    public ItemProcessor<CreateTransactionRequest, Transaction> transactionImportProcessor(AccountRepository accountRepository) {
//        return request -> {
//            Account fromAccount;
//            Account toAccount;
//            BigDecimal amount;
//
//            try {
//                fromAccount = accountRepository.findById(UUID.fromString(request.getFromAccountId())).orElse(new Account());
//                if(fromAccount.getId() == null){
//                    throw new TransactionException(MessageConstant.FROM_ACCOUNT_NOT_EXIST);
//                }
//            } catch (IllegalArgumentException | JpaSystemException e) {
//                throw new TransactionException(MessageConstant.INVALID_FROM_ACCOUNT_UUID);
//            }
//
//            try {
//                toAccount = accountRepository.findById(UUID.fromString(request.getToAccountId())).orElse(new Account());;
//                if(toAccount.getId() == null){
//                    throw new TransactionException(MessageConstant.TO_ACCOUNT_NOT_EXIST);
//                }
//            } catch (IllegalArgumentException | JpaSystemException e) {
//                throw new TransactionException(MessageConstant.INVALID_TO_ACCOUNT_UUID);
//            }
//
//            try {
//                amount = new BigDecimal(request.getAmount());
//                if(fromAccount.getAmount().compareTo(amount) < 0){
//                    throw new TransactionException(MessageConstant.AMOUNT_TRANSFER_EXCEED);
//                }
//            } catch (NumberFormatException e) {
//                throw new TransactionException(MessageConstant.INVALID_AMOUNT_FORMAT);
//            }
//
//            return new TransactionBuilder().fromAccountId(fromAccount).sendToAccountId(toAccount).withAmount(amount).build();
//        };
//    }
//
//    @Bean(name = "transactionImportWriter")
//    public JpaItemWriter<Transaction> transactionImportWriter(@Qualifier("jpaEntityManagerFactory") EntityManagerFactory jpaEntityManagerFactory) {
//        return new JpaItemWriterBuilder<Transaction>().entityManagerFactory(jpaEntityManagerFactory).build();
//    }
//
//
//    @Bean(name = "stepTransactionImport")
//    public Step stepTransactionImport(@Qualifier("transactionImportReader") ItemReader<CreateTransactionRequest> itemReader,
//                                      @Qualifier("transactionImportProcessor") ItemProcessor<CreateTransactionRequest, Transaction> itemProcessor,
//                                      @Qualifier("transactionImportWriter") ItemWriter<Transaction> itemWriter,
//                                      TransactionImportItemListener itemListener,
////                                      TaskExecutor taskExecutor,
//                                      JobRepository jobRepository,@Qualifier("jpaTransactionManager") PlatformTransactionManager jpaTransactionManager) {
//        return new StepBuilder("stepTransactionImport", jobRepository).<CreateTransactionRequest, Transaction>chunk(5, jpaTransactionManager)
//                .reader(itemReader)
//                .processor(itemProcessor)
//                .writer(itemWriter)
////                .taskExecutor(taskExecutor)
//                .listener(itemListener.asItemReaderListener())
//                .listener(itemListener.asItemProcessListener())
//                .listener(itemListener.asItemWriterListener())
//                .faultTolerant()
//                .noRetry(RuntimeException.class)
//                .skip(RuntimeException.class)
//                .skipLimit(Integer.MAX_VALUE)
//                .build();
//    }
//
//    @Bean(name = "transactionReportWriter")
//    public FlatFileItemWriter<Object> transactionReportWriter() {
//        return new FlatFileItemWriterBuilder<>().name("transactionReportWriter").resource(new FileSystemResource(AppConstant.FILE_TRANSACTION_ELEMENT_REPORT_URL))
//                .lineAggregator(new PassThroughLineAggregator<>()).build();
//    }
//
//    @Bean(name = "transactionReportSummary")
//    public Tasklet transactionReportSummary() {
//        return (contribution, chunkContext) -> {
//            Path filePath = Paths.get(AppConstant.FILE_TRANSACTION_SUMMARY_RECORD_URL);
//
//            StepExecution stepExecution = Objects.requireNonNull(StepSynchronizationManager.getContext()).getStepExecution();
//            ExecutionContext context = stepExecution.getJobExecution().getExecutionContext();
//            long transactionSuccess = context.get(AppConstant.SUCCESS_TRANSACTION) == null ? 0L : Long.parseLong(String.valueOf(context.get(AppConstant.SUCCESS_TRANSACTION)));
//            long transactionFail = context.get(AppConstant.ERROR_TRANSACTION) == null ? 0L : Long.parseLong(String.valueOf(context.get(AppConstant.ERROR_TRANSACTION)));
//
//            String summaryContext = "Transaction add success: " + transactionSuccess + "\n"
//                    + "Transaction add fail: " + transactionFail;
//            Files.writeString(filePath, summaryContext, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
//            return RepeatStatus.FINISHED;
//        };
//    }
//
//    @Bean(name = "stepReport")
//    public Step stepReport(@Qualifier("transactionReportReader") ItemReader<String> itemReader,
//                           @Qualifier("transactionReportWriter") ItemWriter<Object> itemWriter,
////                           TaskExecutor taskExecutor,
//                           JobRepository jobRepository,@Qualifier("batchTransactionManager") PlatformTransactionManager transactionManager) {
//        return new StepBuilder("stepReport", jobRepository).chunk(5, transactionManager)
//                .reader(itemReader)
//                .writer(itemWriter)
////                .taskExecutor(taskExecutor)
//                .faultTolerant()
//                .noRetry(RuntimeException.class)
//                .skip(RuntimeException.class)
//                .skipLimit(Integer.MAX_VALUE)
//                .build();
//    }
//
//    @Bean(name = "stepReportSummary")
//    public Step stepReportSummary(@Qualifier("transactionReportSummary") Tasklet tasklet,
//                                  JobRepository jobRepository,@Qualifier("batchTransactionManager") PlatformTransactionManager transactionManager) {
//        return new StepBuilder("stepReportSummary", jobRepository).tasklet(tasklet, transactionManager)
//                .build();
//    }
//
//    @Bean(name = "jobTransactionImport")
//    public Job jobTransactionImport(JobRepository jobRepository,
//                                    @Qualifier("stepTransactionImport") Step stepTransactionImport,
//                                    @Qualifier("stepReport") Step stepReport,
//                                    @Qualifier("stepReportSummary") Step stepReportSummary,
//                                    JobNotificationListener jobNotificationListener) {
//        return new JobBuilder("jobTransactionImport", jobRepository).listener(jobNotificationListener)
//                .start(stepTransactionImport).next(stepReport).next(stepReportSummary).build();
//    }
//
//
////    @Bean
////    public TaskExecutor taskExecutor() {
////        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
////        executor.setCorePoolSize(4);  // Number of threads
////        executor.setMaxPoolSize(8);
////        executor.setQueueCapacity(10);
////        executor.initialize();
////        return executor;
////    }
//}
