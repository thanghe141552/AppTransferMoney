package com.example.constant;

public class AppConstant {
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";
    // File path
    public static final String FILE_TRANSACTION_INSERT_URL = "./file/TransactionInsert.csv";
    public static final String FILE_TRANSACTION_ELEMENT_REPORT_URL = "./file/TransactionElementReport.csv";
    public static final String FILE_TRANSACTION_SUMMARY_RECORD_URL = "./file/TransactionSummaryReport.csv";

    // File delimiter
    public static final String COMMA_DELIMITER = ",";
    public static final String VERTICAL_BAR_DELIMITER = "|";

    // Buffer config
    public static final int BUFFER_SIZE = 1024;

    //Batch context variable
    public static final String TRANSACTION_LIST = "transactionList";
    public static final String SUCCESS_TRANSACTION = "successTransaction";
    public static final String ERROR_TRANSACTION = "errorTransaction";
}
