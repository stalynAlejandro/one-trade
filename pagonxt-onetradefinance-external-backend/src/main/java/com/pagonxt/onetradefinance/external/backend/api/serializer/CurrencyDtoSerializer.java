package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.pagonxt.onetradefinance.external.backend.api.model.CurrencyDto;
import com.pagonxt.onetradefinance.integrations.repository.entity.CurrencyDAO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * This class has a method to convert a DTO class into a entity class and vice versa
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class CurrencyDtoSerializer {

    /**
     * This method converts a currencyDAO object to a currencyDto object
     * @param currency currency object
     * @return currencyDto object
     */
    public CurrencyDto toDto(CurrencyDAO currency) {
        if(currency == null || StringUtils.isBlank(currency.getCurrency())) {
            return null;
        }
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId(currency.getCurrency());
        currencyDto.setCurrency(currency.getCurrency());
        return currencyDto;
    }
}
