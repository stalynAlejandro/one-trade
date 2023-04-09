package com.pagonxt.onetradefinance.integrations.apis.account.serializer;

import com.pagonxt.onetradefinance.integrations.apis.account.model.SantanderAccount;
import com.pagonxt.onetradefinance.integrations.apis.account.model.SantanderAccountListResponse;
import com.pagonxt.onetradefinance.integrations.model.Account;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to serialize Santander accounts data
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class SantanderAccountSerializer {

    /**
     * Class method to serialize a list of Santander accounts
     * @param santanderAccountListResponse SantanderAccountListResponse object
     * @see com.pagonxt.onetradefinance.integrations.model.Account
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.SantanderAccountListResponse
     * @return an empty list or a list of Santander accounts
     */
    public List<Account> toModel(SantanderAccountListResponse santanderAccountListResponse) {
        if(santanderAccountListResponse == null || santanderAccountListResponse.getAccountsDataList() == null) {
            return Collections.emptyList();
        }
        return santanderAccountListResponse.getAccountsDataList().stream().map(this::toModel)
                .collect(Collectors.toList());
    }

    /**
     * Class method to serialize a Santander account
     * @param santanderAccount SantanderAccount object
     * @see com.pagonxt.onetradefinance.integrations.model.Account
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.SantanderAccount
     * @return a Santander account
     */
    public Account toModel(SantanderAccount santanderAccount) {
        if(santanderAccount == null) {
            return new Account();
        }
        Account account = new Account();
        account.setAccountId(santanderAccount.getAccountId());
        account.setCustomerId(santanderAccount.getCustomerId());
        account.setIban(santanderAccount.getDisplayNumber());
        account.setCostCenter(santanderAccount.getCostCenter());
        if(santanderAccount.getMainBalance() != null) {
            account.setCurrency(santanderAccount.getMainBalance().getCurrencyCode());
        }
        return account;
    }
}
