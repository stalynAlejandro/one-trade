package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.CurrencyDto;
import com.pagonxt.onetradefinance.external.backend.api.model.CurrencyResponse;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CurrencyDtoSerializer;
import com.pagonxt.onetradefinance.integrations.repository.entity.CurrencyDAO;
import com.pagonxt.onetradefinance.integrations.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.util.LogUtils.sanitizeLog;

/**
 * Controller with the endpoints to search exchange insurances from the external app
 * @author -
 * @version jdk-11.0.13
 * @see CurrencyService
 * @see CurrencyDtoSerializer
 * @since jdk-11.0.13
 */
@RestController
@Validated
@RequestMapping("${controller.path}/currencies")
public class CurrencyController {

    /**
     *  Logger object for class logs.
     */
    private static final Logger LOG = LoggerFactory.getLogger(CurrencyController.class);

    /**
     * The currency service.
     */
    private final CurrencyService currencyService;

    /**
     * The currency serializer.
     */
    private final CurrencyDtoSerializer currencySerializer;

    /**
     * Constructor class.
     *
     * @param currencyService    : Service to get the currencies
     * @param currencySerializer : Serializer to convert a currencyDAO object to a currencyDto object
     */
    public CurrencyController(CurrencyService currencyService, CurrencyDtoSerializer currencySerializer) {
        this.currencyService = currencyService;
        this.currencySerializer = currencySerializer;
    }

    /**
     * Method to return the list of currencies.
     *
     * @return the list of currencies
     */
    @GetMapping
    @Secured("ROLE_USER")
    public CurrencyResponse getCurrencies(@RequestParam String product,
                                                  @RequestParam String event) {
        List<CurrencyDto> currencies = currencyService.getCurrencyList(product, event)
                .stream().sorted(Comparator.comparing(CurrencyDAO::getCurrency))
                .map(currencySerializer::toDto).filter(Objects::nonNull).collect(Collectors.toList());
        if (LOG.isDebugEnabled()) {
            LOG.debug("getCurrencies(product: {}, event: {}) returned: {}", sanitizeLog(product), sanitizeLog(event), currencies);
        }
        return new CurrencyResponse(currencies);
    }
}
