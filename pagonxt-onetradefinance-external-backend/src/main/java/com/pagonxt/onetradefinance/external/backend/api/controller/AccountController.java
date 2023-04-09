package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.AccountDto;
import com.pagonxt.onetradefinance.external.backend.api.model.AccountSearchResponse;
import com.pagonxt.onetradefinance.external.backend.api.serializer.AccountDtoSerializer;
import com.pagonxt.onetradefinance.integrations.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.util.LogUtils.sanitizeLog;

/**
 * Controller with the endpoints to search client's accounts from the external app
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.service.AccountService
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.AccountDtoSerializer
 * @since jdk-11.0.13
 */

@RestController
@Validated
@RequestMapping("${controller.path}/accounts")
public class AccountController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    /**
     * Class attributes
     */
    private final AccountService accountService;

    private final AccountDtoSerializer accountDtoSerializer;

    /**
     * Account Controller Constructor
     * @param accountService Service that provides necessary functionality to this controller
     * @param accountDtoSerializer Component for the conversion and adaptation of the data structure
     */
    public AccountController(AccountService accountService, AccountDtoSerializer accountDtoSerializer) {
        this.accountService = accountService;
        this.accountDtoSerializer = accountDtoSerializer;
    }

    /**
     * This method gets a client id and returns his/her/its accounts
     * @param client a string with the client id
     * @return a list with the client's accounts with their data
     */
    @GetMapping
    @Secured("ROLE_USER")
    public AccountSearchResponse getAccountsByClientId(@RequestParam @NotEmpty String client) {
        List<AccountDto> accounts = accountService.getCustomerAccounts(client).stream()
                .map(accountDtoSerializer::toDto).collect(Collectors.toList());
        AccountSearchResponse result = AccountSearchResponse.of(client, accounts);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getAccountsByClientId(client: {}) returned: {}", sanitizeLog(client), result);
        }
        return result;
    }
}
