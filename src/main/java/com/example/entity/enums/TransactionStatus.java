package com.example.entity.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum TransactionStatus {
    PENDING("pending"),
    APPROVED("approved"),
    DECLINED("declined"),
    FAILED("failed");

    private final String value;

    TransactionStatus(String value) {
        this.value = value;
    }

    public static TransactionStatus fromValue(String value){
        for (TransactionStatus status:
              TransactionStatus.values()) {
            if(Objects.equals(status.getValue(), value)){
                return status;
            }
        }
        return null;
    }
}
