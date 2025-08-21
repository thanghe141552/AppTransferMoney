package com.example.entity.enums;

public enum AccountType {
    SINGLE_ACCOUNT ("Single Account"),            // For saving money with interest
    BUSINESS_OWNER_ACCOUNT ("Business Owner Account"),           // For company financial operations
    STUDENT_ACCOUNT ("Student Account"),            // For minors or students
    FOREIGN_ACCOUNT ("Foreign Account");  // Holds foreign currency

    private final String value;

    AccountType(String value) {
        this.value = value;
    }
}
