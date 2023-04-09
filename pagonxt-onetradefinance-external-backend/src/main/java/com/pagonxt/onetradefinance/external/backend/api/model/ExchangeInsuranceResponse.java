package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates an object with a list of exchange insurances(dto)
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.ExchangeInsuranceDto
 * @since jdk-11.0.13
 */
public class ExchangeInsuranceResponse extends ArrayList<ExchangeInsuranceDto> {

    /**
     * Class constructor
     * @param exchangeInsurances list of exchange insurances(dto)
     */
    public ExchangeInsuranceResponse(List<ExchangeInsuranceDto> exchangeInsurances) {
        super(exchangeInsurances);
    }
}
