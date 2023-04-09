package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineQuery;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;

import java.util.List;

/**
 * This interface provides a way of a client to interact with some functionality in the application.
 * In this case provides a method to get risk lines
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.RiskLine
 * @see com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineQuery
 * @since jdk-11.0.13
 */
public interface RiskLineService {

    /**
     * This method allows to obtain a list of risk lines of a customer
     * @param query a RiskLineQuery object
     * @return a list of risk lines
     */
    List<RiskLine> getCustomerRiskLines(RiskLineQuery query);

    /**
     * This method allows to obtain a risk line through an id
     * @param query a RiskLineQuery object
     * @return a risk line
     */
    RiskLine getRiskLineById(RiskLineQuery query);
}
