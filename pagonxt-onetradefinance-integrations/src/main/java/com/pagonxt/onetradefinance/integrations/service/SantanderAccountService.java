package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.apis.account.AccountGateway;
import com.pagonxt.onetradefinance.integrations.apis.account.serializer.SantanderAccountSerializer;
import com.pagonxt.onetradefinance.integrations.model.Account;

import java.util.List;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get Santander accounts
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.Account
 * @see AccountService
 * @see com.pagonxt.onetradefinance.integrations.apis.account.AccountGateway
 * @see  com.pagonxt.onetradefinance.integrations.apis.account.serializer.SantanderAccountSerializer
 * @since jdk-11.0.13
 */
public class SantanderAccountService implements AccountService {

    private final AccountGateway accountGateway;

    private final SantanderAccountSerializer santanderAccountSerializer;

    /**
     * constructor method
     * @param accountGateway             : an AccountGateway object
     * @param santanderAccountSerializer : an SantanderAccountSerializer
     */
    public SantanderAccountService(AccountGateway accountGateway,
                                   SantanderAccountSerializer santanderAccountSerializer) {
        this.accountGateway = accountGateway;
        this.santanderAccountSerializer = santanderAccountSerializer;
    }

    /**
     * This method allows to obtain a list of accounts of a customer
     * @param customerId a string with the customer id
     * @return a list of accounts of a customer
     */
    @Override
    public List<Account> getCustomerAccounts(String customerId) {
        return santanderAccountSerializer.toModel(accountGateway.getCustomerAccounts(customerId));
    }

    /**
     * This method allows to obtain an account through an account ID
     * @param id a string with the account id
     * @return an Account object with the account
     */
    @Override
    public Account getAccountById(String id) {
        return santanderAccountSerializer.toModel(accountGateway.getAccountById(id));
    }
}
