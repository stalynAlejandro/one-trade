package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates an object with a list of currencies(dto)
 * @author -
 * @version jdk-11.0.13
 * @see CurrencyDto
 * @since jdk-11.0.13
 */
public class CurrencyResponse extends ArrayList<CurrencyDto> {

    /**
     * Class constructor
     * @param currencies list of currencies(dto)
     */
    public CurrencyResponse(List<CurrencyDto> currencies) {
        super(currencies);
    }
}
