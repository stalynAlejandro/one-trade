package com.pagonxt.onetradefinance.work.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import com.pagonxt.onetradefinance.work.common.CaseCommonVariablesHelper;
import com.pagonxt.onetradefinance.work.service.model.ExchangeInsuranceFlowable;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;

/**
 * Class with some methods to serialize and deserialize data of exchange insurances
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class ExchangeInsuranceFlowableMapper {

    //class attribute
    private final DateFormat dateTimeFormat;

    /**
     * Constructor method
     */
    public ExchangeInsuranceFlowableMapper() {
        // Flowable needs Zulu Time for this object
        dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Class method to convert data
     * @param dataObject : a DataObjectInstanceVariableContainer object
     * @see com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer
     * @return a list of exchange insurances
     * @throws JsonProcessingException handles Json Processing exceptions
     */
    public List<ExchangeInsurance> toModel(DataObjectInstanceVariableContainer dataObject)
            throws JsonProcessingException {
        if (dataObject == null) {
            return Collections.emptyList();
        }
        Object exchangeInsuranceListObject = dataObject.getVariable(REQUEST_ADVANCE_EXCHANGE_INSURANCE);
        if (exchangeInsuranceListObject == null) {
            return Collections.emptyList();
        }
        List<ExchangeInsuranceFlowable> exchangeInsuranceFlowableList =
                CaseCommonVariablesHelper.convertToExchangeInsuranceList(exchangeInsuranceListObject);
        List<ExchangeInsurance> exchangeInsurances = new ArrayList<>();
        String buyCurrency = dataObject.getString(EXCHANGE_INSURANCE_BUY_CURRENCY);
        String sellCurrency = dataObject.getString(EXCHANGE_INSURANCE_SELL_CURRENCY);
        for(ExchangeInsuranceFlowable exchangeInsuranceFlowable : exchangeInsuranceFlowableList) {
            ExchangeInsurance exchangeInsurance = toModel(exchangeInsuranceFlowable);
            if(exchangeInsurance != null) {
                exchangeInsurance.setBuyCurrency(buyCurrency);
                exchangeInsurance.setSellCurrency(sellCurrency);
            }
            exchangeInsurances.add(exchangeInsurance);
        }
        return exchangeInsurances.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Class method to convert data
     * @param exchangeInsuranceFlowable : a ExchangeInsuranceFlowable object
     * @see com.pagonxt.onetradefinance.work.service.model.ExchangeInsuranceFlowable
     * @see com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance
     * @return a ExchangeInsurance object
     */
    public ExchangeInsurance toModel(ExchangeInsuranceFlowable exchangeInsuranceFlowable) {
        if (exchangeInsuranceFlowable == null) {
            return null;
        }
        ExchangeInsurance exchangeInsurance = new ExchangeInsurance();
        exchangeInsurance.setExchangeInsuranceId(exchangeInsuranceFlowable.getId());
        exchangeInsurance.setType(exchangeInsuranceFlowable.getType());
        String useDate = exchangeInsuranceFlowable.getUseDate();
        if(Strings.isNotBlank(exchangeInsuranceFlowable.getUseDate())) {
            try {
                exchangeInsurance.setUseDate(dateTimeFormat.parse(useDate));
            } catch (ParseException e) {
                throw new DateFormatException(useDate, e);
            }
        }
        exchangeInsurance.setBuyAmount(exchangeInsuranceFlowable.getBuyAvailableAmount());
        exchangeInsurance.setSellAmount(exchangeInsuranceFlowable.getSellAvailableAmount());
        exchangeInsurance.setExchangeRate(exchangeInsuranceFlowable.getExchangeRate());
        exchangeInsurance.setUseAmount(exchangeInsuranceFlowable.getAmountToUse());
        return exchangeInsurance;
    }

    /**
     * Class method to convert data
     * @param exchangeInsurances : a list of exchange insurances
     * @see com.pagonxt.onetradefinance.work.service.model.ExchangeInsuranceFlowable
     * @see com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance
     * @return a list of exchange insurances (flowable)
     */
    public List<ExchangeInsuranceFlowable> toFlowable(List<ExchangeInsurance> exchangeInsurances) {
        if(exchangeInsurances == null) {
            return Collections.emptyList();
        }
        List<ExchangeInsuranceFlowable> exchangeInsuranceFlowableList = new ArrayList<>();
        for(ExchangeInsurance exchangeInsurance : exchangeInsurances) {
            ExchangeInsuranceFlowable exchangeInsuranceFlowable = toFlowable(exchangeInsurance);
            exchangeInsuranceFlowableList.add(exchangeInsuranceFlowable);
        }
        return exchangeInsuranceFlowableList.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Class method to convert data
     * @param exchangeInsurance : a ExchangeInsurance object
     * @see com.pagonxt.onetradefinance.work.service.model.ExchangeInsuranceFlowable
     * @see com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance
     * @return a  ExchangeInsuranceFlowable object
     */
    public ExchangeInsuranceFlowable toFlowable(ExchangeInsurance exchangeInsurance) {
        if (exchangeInsurance == null) {
            return null;
        }
        ExchangeInsuranceFlowable exchangeInsuranceFlowable = new ExchangeInsuranceFlowable();
        exchangeInsuranceFlowable.setId(exchangeInsurance.getExchangeInsuranceId());
        exchangeInsuranceFlowable.setType(exchangeInsurance.getType());
        if(exchangeInsurance.getUseDate() != null) {
            exchangeInsuranceFlowable.setUseDate(dateTimeFormat.format(exchangeInsurance.getUseDate()));
        }
        exchangeInsuranceFlowable.setBuyAvailableAmount(exchangeInsurance.getBuyAmount());
        exchangeInsuranceFlowable.setSellAvailableAmount(exchangeInsurance.getSellAmount());
        exchangeInsuranceFlowable.setExchangeRate(exchangeInsurance.getExchangeRate());
        exchangeInsuranceFlowable.setAmountToUse(exchangeInsurance.getUseAmount());
        return exchangeInsuranceFlowable;
    }

}
