package com.example.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.KeysetScrollPosition;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterScrollingTransactionResponse {
    private List<FilterTransactionResponse> transactionResponses;
    private boolean hasNext;
    private KeysetScrollPosition nextPosition;
}
