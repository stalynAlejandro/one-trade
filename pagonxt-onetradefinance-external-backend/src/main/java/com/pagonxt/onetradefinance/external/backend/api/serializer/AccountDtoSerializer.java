package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.pagonxt.onetradefinance.external.backend.api.model.AccountDto;
import com.pagonxt.onetradefinance.integrations.model.Account;
import org.springframework.stereotype.Component;

/**
 * This class has methods to convert a DTO class into an entity class and viceversa
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class AccountDtoSerializer {

    /**
     * This method converts an account object to an AccountDto object
     * @param account account object
     * @return accountDto object
     */
    public AccountDto toDto(Account account) {
        if(account == null) {
            return null;
        }
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getAccountId());
        accountDto.setIban(account.getIban());
        accountDto.setCurrency(account.getCurrency());
        return accountDto;
    }

    /**
     * This method converts an accountDto object to an account object
     * @param accountDto accountDto object
     * @return account object
     */
    public Account toModel(AccountDto accountDto) {
        if(accountDto == null) {
            return new Account();
        }
        Account account = new Account();
        account.setAccountId(accountDto.getId());
        account.setIban(accountDto.getIban());
        account.setCurrency(accountDto.getCurrency());
        return account;
    }
}
