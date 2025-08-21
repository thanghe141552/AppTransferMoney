package com.example.mapper;

import com.example.entity.AccountHolder;
import com.example.entity.request.CreateAccountHolderRequest;
import com.example.entity.response.CreateAccountHolderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {AccountHolderMapperHelper.class})
public interface AccountHolderMapper {
    @Mappings({
            @Mapping(target = "activeStatus", ignore = true),
            @Mapping(target = "accounts", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    AccountHolder createAccountHolderRequestToAccountHolder(CreateAccountHolderRequest createAccountHolderRequest);

    @Mapping(target = "accountHolderId", source = "id")
    CreateAccountHolderResponse accountHolderToCreateAccountHolderResponse(AccountHolder accountHolder);
}
