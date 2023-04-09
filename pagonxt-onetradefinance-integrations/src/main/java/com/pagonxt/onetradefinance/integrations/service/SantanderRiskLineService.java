package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.apis.riskline.RiskLineGateway;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineQuery;
import com.pagonxt.onetradefinance.integrations.apis.riskline.serializer.SantanderRiskLineSerializer;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;

import java.util.List;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get risk lines from Santander
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.RiskLine
 * @see RiskLineService
 * @see com.pagonxt.onetradefinance.integrations.apis.riskline.RiskLineGateway
 * @see  com.pagonxt.onetradefinance.integrations.apis.riskline.serializer.SantanderRiskLineSerializer
 * @since jdk-11.0.13
 */
public class SantanderRiskLineService implements RiskLineService {

    private final RiskLineGateway riskLineGateway;

    private final SantanderRiskLineSerializer santanderRiskLineSerializer;

    /**
     * constructor method
     * @param riskLineGateway             : a RiskLineGateway object
     * @param santanderRiskLineSerializer : a SantanderRiskLineSerializer object
     */
    public SantanderRiskLineService(RiskLineGateway riskLineGateway,
                                    SantanderRiskLineSerializer santanderRiskLineSerializer) {
        this.riskLineGateway = riskLineGateway;
        this.santanderRiskLineSerializer = santanderRiskLineSerializer;
    }

    /**
     * This method allows to obtain a list of risk lines of a customer
     * @param query a RiskLineQuery object
     * @return a list of risk lines
     */
    @Override
    public List<RiskLine> getCustomerRiskLines(RiskLineQuery query) {
        return santanderRiskLineSerializer.toModel(riskLineGateway.getRiskLineByCostumerId(query));
    }

    /**
     * This method allows to obtain a risk line through an id
     * @param query a RiskLineQuery object
     * @return a risk line
     */
    @Override
    public RiskLine getRiskLineById(RiskLineQuery query) {
        return santanderRiskLineSerializer.toModel(riskLineGateway.getRiskLineById(query));
    }
}
