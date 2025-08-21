package com.example.entity.request;

import com.example.config.instant.InstantDateOnlyDeserializer;
import com.example.config.instant.InstantDateOnlySerializer;
import com.example.entity.enums.TransactionStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterTransactionRequest {
    private List<TransactionStatus> statusList;
    private BigDecimal amountMin;
    private BigDecimal amountMax;
//    @JsonSerialize(using = InstantDateOnlySerializer.class)
//    @JsonDeserialize(using = InstantDateOnlyDeserializer.class)
    private LocalDateTime createdDateFrom;
//    @JsonSerialize(using = InstantDateOnlySerializer.class)
//    @JsonDeserialize(using = InstantDateOnlyDeserializer.class)
    private LocalDateTime createdDateTo;
}
