package com.example.mapper;

import com.example.entity.Transaction;
import com.example.entity.auditLog.TransactionAuditLog;
import com.example.entity.request.CreateTransactionRequest;
import com.example.entity.response.CreateTransactionResponse;
import com.example.entity.response.FilterTransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {TransactionMapperHelper.class})
public interface TransactionMapper {

    @Mappings({
            @Mapping(target = "transactionStatus", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "activeStatus", ignore = true),
            @Mapping(target = "toAccount", source = "toAccountId", qualifiedByName = "toAccount"),
            @Mapping(target = "fromAccount", source = "fromAccountId", qualifiedByName = "toAccount")
    })
    Transaction createTransactionRequestToTransaction(CreateTransactionRequest createTransactionRequest);

    @Mappings({
            @Mapping(target = "failMessage", ignore = true),
            @Mapping(target = "fromAccountId", source = "fromAccount", qualifiedByName = "toAccountId"),
            @Mapping(target = "toAccountId", source = "toAccount", qualifiedByName = "toAccountId"),
            @Mapping(target = "transactionId", source = "id")
    })
    CreateTransactionResponse transactionToCreateTransactionResponse(Transaction transaction);

    @Mappings({
            @Mapping(target = "transactionStatus", ignore = true),
            @Mapping(target = "toAccount", source = "toAccountId", qualifiedByName = "toAccount"),
            @Mapping(target = "fromAccount", source = "fromAccountId", qualifiedByName = "toAccount"),
            @Mapping(target = "activeStatus", ignore = true),
            @Mapping(target = "id", source = "transactionId")
    })
    Transaction createTransactionResponseToTransaction(CreateTransactionResponse createTransactionResponse);

    @Mappings({
            @Mapping(target = "toAccountStatus", source = "toAccount", qualifiedByName = "toAccountStatus"),
            @Mapping(target = "toAccountName", source = "toAccount", qualifiedByName = "toAccountName"),
            @Mapping(target = "fromAccountStatus", source = "fromAccount", qualifiedByName = "toAccountStatus"),
            @Mapping(target = "fromAccountName", source = "fromAccount", qualifiedByName = "toAccountName"),
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "toAccountId", source = "toAccount", qualifiedByName = "toAccountId"),
            @Mapping(target = "fromAccountId", source = "fromAccount", qualifiedByName = "toAccountId")
    })
    FilterTransactionResponse transactionToFilterTransactionResponse(Transaction transaction);

    @Mappings({
            @Mapping(target = "toAccountId", source = "toAccount", qualifiedByName = "toAccountId"),
            @Mapping(target = "fromAccountId", source = "fromAccount", qualifiedByName = "toAccountId")
    })
    TransactionAuditLog transactionToTransactionAuditLog(Transaction transaction);
}
