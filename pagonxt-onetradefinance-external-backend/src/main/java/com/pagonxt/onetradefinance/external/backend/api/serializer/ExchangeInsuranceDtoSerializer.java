package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.pagonxt.onetradefinance.external.backend.api.model.ExchangeInsuranceDto;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.util.ParseUtils;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * This class has methods to convert DTO's into entities and viceversa, for exchanges insurances
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class ExchangeInsuranceDtoSerializer {

    /**
     * Class Variables
     */
    private final DateFormat dateTimeFormat;

    /**
     * Class constructor
     * @param dateFormatProperties In this case, we use this object to get the date format
     */
    public ExchangeInsuranceDtoSerializer(DateFormatProperties dateFormatProperties) {
        dateTimeFormat = dateFormatProperties.getDateFormatInstance();
    }

    /**
     * Method to convert a exchangeInsuranceDto object to a exchangeInsurance object
     * @param exchangeInsuranceDto exchangeInsuranceDto object
     * @see com.pagonxt.onetradefinance.external.backend.api.model.ExchangeInsuranceDto
     * @return exchangeInsurance object
     */
    public ExchangeInsurance toModel(ExchangeInsuranceDto exchangeInsuranceDto) {
        if (exchangeInsuranceDto == null) {
            return new ExchangeInsurance();
        }
        ExchangeInsurance exchangeInsurance = new ExchangeInsurance();
        exchangeInsurance.setExchangeInsuranceId(exchangeInsuranceDto.getExchangeInsuranceId());
        exchangeInsurance.setType(exchangeInsuranceDto.getType());
        if (Strings.isNotBlank(exchangeInsuranceDto.getUseDate())) {
            try {
                exchangeInsurance.setUseDate(dateTimeFormat.parse(exchangeInsuranceDto.getUseDate()));
            } catch (ParseException e) {
                throw new DateFormatException(exchangeInsuranceDto.getUseDate(), e);
            }
        }
        if (exchangeInsuranceDto.getSellAmount() != null) {
            exchangeInsurance.setSellAmount(Double.parseDouble(exchangeInsuranceDto.getSellAmount()));
        }
        exchangeInsurance.setSellCurrency(exchangeInsuranceDto.getSellCurrency());
        if (exchangeInsuranceDto.getBuyAmount() != null) {
            exchangeInsurance.setBuyAmount(Double.parseDouble(exchangeInsuranceDto.getBuyAmount()));
        }
        exchangeInsurance.setBuyCurrency(exchangeInsuranceDto.getBuyCurrency());
        if (exchangeInsuranceDto.getExchangeRate() != null) {
            exchangeInsurance.setExchangeRate(Double.parseDouble(exchangeInsuranceDto.getExchangeRate()));
        }
        if (exchangeInsuranceDto.getUseAmount() != null) {
            exchangeInsurance.setUseAmount(Double.parseDouble(exchangeInsuranceDto.getUseAmount()));
        }
        return exchangeInsurance;
    }

    /**
     * Method to convert a exchangeInsurance object to a exchangeInsuranceDto object
     * @param exchangeInsurance exchangeInsurance object
     * @see com.pagonxt.onetradefinance.external.backend.api.model.ExchangeInsuranceDto
     * @return exchangeInsuranceDto object
     */
    public ExchangeInsuranceDto toDto(ExchangeInsurance exchangeInsurance) {
        if(exchangeInsurance == null) {
            return null;
        }
        ExchangeInsuranceDto exchangeInsuranceDto = new ExchangeInsuranceDto();
        exchangeInsuranceDto.setExchangeInsuranceId(exchangeInsurance.getExchangeInsuranceId());
        exchangeInsuranceDto.setType(exchangeInsurance.getType());
        if (exchangeInsurance.getUseDate() != null) {
            exchangeInsuranceDto.setUseDate(dateTimeFormat.format(exchangeInsurance.getUseDate()));
        }
        if (exchangeInsurance.getSellAmount() != null) {
            exchangeInsuranceDto.setSellAmount(ParseUtils.parseDoubleToString(exchangeInsurance.getSellAmount()));
        }
        exchangeInsuranceDto.setSellCurrency(exchangeInsurance.getSellCurrency());
        if (exchangeInsurance.getBuyAmount() != null) {
            exchangeInsuranceDto.setBuyAmount(ParseUtils.parseDoubleToString(exchangeInsurance.getBuyAmount()));
        }
        exchangeInsuranceDto.setBuyCurrency(exchangeInsurance.getBuyCurrency());
        if (exchangeInsurance.getExchangeRate() != null) {
            exchangeInsuranceDto.setExchangeRate(String.format(Locale.ROOT, "%.4f", exchangeInsurance.getExchangeRate()));
        }
        if (exchangeInsurance.getUseAmount() != null) {
            exchangeInsuranceDto.setUseAmount(ParseUtils.parseDoubleToString(exchangeInsurance.getUseAmount()));
        }
        return exchangeInsuranceDto;
    }

}
