package com.example.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.KeysetScrollPosition;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterScrollingTransactionRequest {
    private KeysetScrollPosition position;
}
