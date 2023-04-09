package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class returns a list of customers(dto)
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto
 * @since jdk-11.0.13
 */
public class CustomerSearchResponse extends ArrayList<CustomerDto> {

    /**
     * Class constructor
     * @param clients list of customers
     */
    public CustomerSearchResponse(List<CustomerDto> clients) {
        super(clients);
    }
}
