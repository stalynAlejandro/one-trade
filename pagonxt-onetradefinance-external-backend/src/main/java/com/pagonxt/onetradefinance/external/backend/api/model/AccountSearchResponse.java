package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class create an object with a list of accounts by a client
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.CustomerAccountList
 * @since jdk-11.0.13
 */
public class AccountSearchResponse extends ArrayList<CustomerAccountList> {

    /**
     * This method returns a list of accounts by a client
     * @param client a client id
     * @param accounts list of accounts
     * @return a response with a list of account by a client
     */
    public static AccountSearchResponse of(String client, List<AccountDto> accounts) {
        return new AccountSearchResponse(List.of(new CustomerAccountList(client, accounts)));
    }

    /**
     * Class constructor
     * @param accounts list of accounts
     */
    public AccountSearchResponse(List<CustomerAccountList> accounts) {
        super(accounts);
    }
}
