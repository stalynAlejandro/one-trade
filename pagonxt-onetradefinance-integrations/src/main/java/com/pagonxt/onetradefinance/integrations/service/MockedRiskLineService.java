package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineQuery;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get mocked risk lines
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.RiskLine
 * @see RiskLineService
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
public class MockedRiskLineService implements RiskLineService {

    /**
     * A Logger object is used to log messages for a specific system or application component
     */
    private static final Logger LOG = LoggerFactory.getLogger(MockedRiskLineService.class);

    private final List<RiskLine> riskLines = new ArrayList<>();

    /**
     * constructor method
     * @param mapper an ObjectMapper object, that provides functionality for reading and writing JSON
     */
    public MockedRiskLineService(ObjectMapper mapper) {
        try (InputStream resourceStream = this.getClass().getClassLoader()
                .getResourceAsStream("mock-data/risk-lines.json")) {
            this.riskLines.addAll(mapper.readValue(resourceStream, new TypeReference<>() {}));
            LOG.debug("Mock risk lines repository initialized");
        } catch (Exception e) {
            LOG.warn("Error reading risk lines file, mock repository will be empty", e);
        }
    }

    /**
     * This method allows to obtain a list of risk lines of a customer
     * @param query a RiskLineQuery object
     * @return a list of risk lines
     */
    @Override
    public List<RiskLine> getCustomerRiskLines(RiskLineQuery query) {
        return riskLines.parallelStream()
                .filter(riskLine -> riskLine.getClient().equals(query.getCustomerId()))
                .collect(Collectors.toList());
    }

    /**
     * This method allows to obtain a risk line through an id
     * @param query a RiskLineQuery object
     * @return a risk line
     */
    @Override
    public RiskLine getRiskLineById(RiskLineQuery query) {
        return riskLines.parallelStream()
                .filter(riskLine -> riskLine.getRiskLineId().equals(query.getRiskLineId()))
                .findFirst().orElse(null);
    }
}
