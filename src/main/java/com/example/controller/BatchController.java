//package com.example.controller;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.configuration.JobRegistry;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.launch.NoSuchJobException;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("/batch")
//public class BatchController {
//
//    private final JobLauncher jobLauncher;
//    private final JobRegistry jobRegistry;
//
//    @Autowired
//    public BatchController(JobLauncher jobLauncher, JobRegistry jobRegistry) {
//        this.jobLauncher = jobLauncher;
//        this.jobRegistry = jobRegistry;
//    }
//
//    @PostMapping("/run/{jobName}")
//    public ResponseEntity<String> runJob(@PathVariable String jobName) {
//        try {
//            Job job = jobRegistry.getJob(jobName);
//
//            JobParameters jobParameters = new JobParametersBuilder()
//                    .addLong("timeStart", System.currentTimeMillis())
//                    .toJobParameters();
//
//            jobLauncher.run(job, jobParameters);
//
//            return ResponseEntity.ok("Job '" + jobName + "' triggered.");
//        } catch (NoSuchJobException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found: " + jobName);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to trigger job: " + e.getMessage());
//        }
//    }
//
//
//    @GetMapping("/listAllJob")
//    public ResponseEntity<List<String>> listJobs() {
//        return ResponseEntity.ok(new ArrayList<>(jobRegistry.getJobNames()));
//    }
//
//}
