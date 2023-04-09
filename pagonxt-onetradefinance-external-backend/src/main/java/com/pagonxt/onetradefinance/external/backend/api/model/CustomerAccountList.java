package com.pagonxt.onetradefinance.external.backend.api.model;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * This class create an object with a list of accounts by a client
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.AccountDto
 * @since jdk-11.0.13
 */
public class CustomerAccountList {

    //Class attributes
    private final String client;

    private final List<AccountDto> accounts;

    /**
     * Class constructor
     * @param client client id
     * @param accounts list if accounts
     */
    public CustomerAccountList(String client, @NotNull List<AccountDto> accounts) {
        this.client = client;
        this.accounts = accounts;
    }

    /**
     * getter method
     * @return the client id
     */
    public String getClient() {
        return client;
    }

    /**
     * getter method
     * @return a list of the accounts by  a client
     */
    public List<AccountDto> getAccounts() {
        return accounts;
    }

    /**
     * Equals method
     * @param o an object
     * @return true if the objects are equals, or not if they aren't equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomerAccountList that = (CustomerAccountList) o;
        return client.equals(that.client) && accounts.equals(that.accounts);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(client, accounts);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ClientAccountList{" +
                "client='" + client + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}
