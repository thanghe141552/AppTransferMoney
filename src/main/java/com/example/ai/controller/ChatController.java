//package com.example.ai.controller;
//
//import io.github.bucket4j.Bandwidth;
//import io.github.bucket4j.Bucket;
//import io.github.bucket4j.Refill;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.Duration;
//
//@RestController
//@RequestMapping("/myChatGpt")
//public class ChatController {
//    private final ChatClient chatClient;
//    private final Bucket bucket;
//    public ChatController(@Qualifier("myChatClient") ChatClient chatClient) {
//        this.chatClient = chatClient;
//        // Allow 5 requests per second
//        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofSeconds(1)));
//        this.bucket = Bucket.builder().addLimit(limit).build();
//    }
//
//    @GetMapping("/message")
//    String generation(String userInput) {
////        if (bucket.tryConsume(1)) {
////        return this.chatClient.prompt()
////                .user(userInput)
////                .call()
////                .content();
////        } else {
////            return "Rate limit exceeded. Try again later.";
////        }
//
//        for (int i = 0; i < 5; i++) {
//            try {
//                return chatClient.prompt().user(userInput).call().content();
//            } catch (Exception e) {
//                try {
//                    Thread.sleep(1000 * (i + 1)); // exponential backoff
//                } catch (InterruptedException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        }
//        return userInput;
//    }
//
//    @GetMapping("/test")
//    public String test() {
//        return "OK";
//    }
//}
