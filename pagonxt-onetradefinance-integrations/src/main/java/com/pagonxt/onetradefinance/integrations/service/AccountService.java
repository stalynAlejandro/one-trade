package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.model.Account;

import java.util.List;

/**
 * This interface provides a way of a client to interact with some functionality in the application.
 * In this case provides two method to get accounts
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.Account
 * @since jdk-11.0.13
 */
public interface AccountService {

    /**
     * This method allows to obtain a list of accounts of a user
     * @param customerId a string with the customer id
     * @return a list of accounts of a user
     */
    List<Account> getCustomerAccounts(String customerId);

    /**
     * This method allows to obtain an account through an account ID
     * @param id a string with the account id
     * @return an Account object with the account
     */
    Account getAccountById(String id);
}
