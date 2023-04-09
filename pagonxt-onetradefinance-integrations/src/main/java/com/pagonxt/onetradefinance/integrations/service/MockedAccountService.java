package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some method to get mocked accounts
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.Account
 * @see AccountService
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
public class MockedAccountService implements AccountService {

    /**
     * A Logger object is used to log messages for a specific system or application component
     */
    private static final Logger LOG = LoggerFactory.getLogger(MockedAccountService.class);

    private final List<Account> accounts = new ArrayList<>();

    /**
     * constructor method
     * @param mapper an ObjectMapper object, that provides functionality for reading and writing JSON
     */
    public MockedAccountService(ObjectMapper mapper) {
        try (InputStream resourceStream = this.getClass().getClassLoader()
                .getResourceAsStream("mock-data/accounts.json")) {
            accounts.addAll(mapper.readValue(resourceStream, new TypeReference<>() {}));
            LOG.debug("Mock accounts repository initialized with {} items", accounts.size());
        } catch (Exception e) {
            LOG.warn("Error reading accounts file, mock repository will be empty", e);
        }
    }

    /**
     * This method allows to obtain a list of accounts of a user
     * @param customerId a string with the customer id
     * @return a list of accounts of a user
     */
    public List<Account> getCustomerAccounts(String customerId) {
        return accounts.parallelStream()
                .filter(account -> customerId.equals(account.getCustomerId()))
                .collect(Collectors.toList());
    }

    /**
     * This method allows to obtain an account through an account ID
     * @param id a string with the account id
     * @return an Account object with the account
     */
    @Override
    public Account getAccountById(String id) {
        return accounts.parallelStream()
                .filter(account -> account.getAccountId().equals(id))
                .findFirst().orElse(null);
    }
}
