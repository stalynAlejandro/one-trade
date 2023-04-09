package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerSearchResponse;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.integrations.model.CustomerQuery;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
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
 * Controller with the endpoints to search customers from the external app
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.service.CustomerService
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.CommentDtoSerializer
 * @since jdk-11.0.13
 */
@Validated
@RestController
@RequestMapping("${controller.path}/clients")
public class ClientController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);

    /**
     * Class attributes
     */
    private final CustomerService customerService;
    private final CustomerDtoSerializer customerDtoSerializer;

    /**
     * Client Controller Constructor
     * @param clientService Service that provides necessary functionality to this controller
     * @param customerDtoSerializer Component for the conversion and adaptation of the data structure
     */
    public ClientController(CustomerService clientService, CustomerDtoSerializer customerDtoSerializer) {
        this.customerService = clientService;
        this.customerDtoSerializer = customerDtoSerializer;
    }

    /**
     * This method get some parameters to return a list of customers
     * @param name a string with the customer's name. It's required
     * @param taxId a string with Tax Identification Number (DNI, NIE...)
     * @param personNumber a string with the internal number
     * @param office a string with the customer's office
     * @return a list of customers with their data
     */
    @GetMapping
    @Secured("ROLE_USER")
    public CustomerSearchResponse searchClients(@RequestParam @NotEmpty String name,
                                                @RequestParam(name = "tax_id", required = false) String taxId,
                                                @RequestParam(name = "person_number", required = false) String personNumber,
                                                @RequestParam(required = false) String office) {
        CustomerQuery query = new CustomerQuery(name, taxId, personNumber, office);
        List<CustomerDto> clients = customerService.searchCustomers(query).stream()
                .map(customerDtoSerializer::toDto).collect(Collectors.toList());
        CustomerSearchResponse result = new CustomerSearchResponse(clients);
        if (LOG.isDebugEnabled()) {
            LOG.debug("searchClients(query: {}) returned: {}", sanitizeLog(query), result);
        }
        return result;
    }
}
