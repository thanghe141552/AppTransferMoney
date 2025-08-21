package com.example.mapper;

import com.example.entity.Account;
import com.example.entity.auditLog.AccountAuditLog;
import com.example.entity.request.CreateAccountRequest;
import com.example.entity.response.CreateAccountResponse;
import com.example.entity.response.FilterAccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {AccountMapperHelper.class})
public interface AccountMapper {
    @Mappings({
            @Mapping(target = "userId", source = "user"),
            @Mapping(target = "accountHolderId", source = "accountHolder"),
            @Mapping(target = "accountId", source = "id")
    })
    CreateAccountResponse accountToCreateAccountResponse(Account account);

    @Mappings({
            @Mapping(target = "accountHolder", source = "accountHolderId"),
            @Mapping(target = "activeStatus", ignore = true),
            @Mapping(target = "user", source = "userId"),
            @Mapping(target = "transactionsAsFromAccount", ignore = true),
            @Mapping(target = "transactionsAsToAccount", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    Account createAccountRequestToAccount(CreateAccountRequest createAccountRequest);

    @Mapping(target = "userId", source = "user")
    @Mapping(target = "accountHolderId", source = "accountHolder")
    @Mapping(target = "accountId", source = "id")
    @Mapping(target = "amountSpentInPeriod", ignore = true)
    FilterAccountResponse accountToFilterAccountResponse(Account account);

    @Mappings({
            @Mapping(target = "userId", source = "user"),
            @Mapping(target = "accountHolderId", source = "accountHolder"),
            @Mapping(target = "currentAmount", ignore = true),
            @Mapping(target = "amountTransferred", ignore = true),
            @Mapping(target = "amountReceived", ignore = true),
            @Mapping(target = "accountId", source = "id")
    })
    AccountAuditLog accountToAccountAuditLog(Account account);
}
