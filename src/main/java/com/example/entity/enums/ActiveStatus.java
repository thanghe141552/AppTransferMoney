package com.example.entity.enums;

import lombok.Getter;

@Getter
public enum ActiveStatus {
    DELETED(0),
    ACTIVE(1);

    private final int value;

    ActiveStatus(int value) {
        this.value = value;
    }

}
