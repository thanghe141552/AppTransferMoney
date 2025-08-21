//package com.example.socket;
//
//import com.example.entity.Transaction;
//import com.example.service.TransactionService;
//import com.example.service.impl.TransactionServiceImpl;
//
//import java.io.*;
//import java.math.BigDecimal;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.List;
//
//public class TransactionSocketHandler {
//    private static final int PORT = 611;
//
//    public static void startServer() {
//        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//            System.out.println("Server started...");
//            while (true) {
//                try (Socket clientSocket = serverSocket.accept()) {
//                    InputStream inputStream = clientSocket.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                    String clientRequest = reader.readLine();
//
//                    TransactionService transactionService = new TransactionServiceImpl();
//
//                    if (clientRequest.startsWith("CREATE TX")) {
//                        String[] parts = clientRequest.split(",");
//                        int fromUserId = Integer.parseInt(parts[1]);
//                        int toUserId = Integer.parseInt(parts[2]);
//                        BigDecimal amount = BigDecimal.valueOf(Long.parseLong(parts[3]));
//
//                        //User input
////                        transactionService.createTransaction(fromUserId, toUserId, amount);
////
////                        //Fake from user input
////                        transactionService.createTransaction(fromUserId + 2, toUserId + 2,  amount.multiply(BigDecimal.valueOf(10L)));
////                        transactionService.createTransaction(fromUserId + 3, toUserId + 3, amount.multiply(BigDecimal.valueOf(5L)));
////                        transactionService.createTransaction(fromUserId + 4, toUserId + 4, amount.multiply(BigDecimal.valueOf(2L)));
////                        transactionService.createTransaction(fromUserId + 5, toUserId + 9, amount.multiply(BigDecimal.valueOf(7L)));
//
//                    } else if (clientRequest.startsWith("READ TX")) {
//
//                        List<Transaction> transactionList = transactionService.getTransactionHistory();
//                        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
//                        for (Transaction tran:transactionList) {
//                            writer.println(tran.toString());
//                        }
//
//                    }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        startServer();
//    }
//}
