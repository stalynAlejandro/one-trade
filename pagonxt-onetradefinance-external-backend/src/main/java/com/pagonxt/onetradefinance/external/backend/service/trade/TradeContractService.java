package com.pagonxt.onetradefinance.external.backend.service.trade;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.*;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContract;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContractsQuery;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pagonxt.onetradefinance.integrations.constants.TradeConstants.CONTRACT_TYPE_CLI_REQUEST;

/**
 * This Service class provides a way of a client to interact with some functionality in the application.
 * @author -
 * @version jdk-11.0.13
 * @see RestTemplateWorkService
 * @since jdk-11.0.13
 */
@Service
public class TradeContractService {

    /**
     * Class attributes
     */
    private final RestTemplateWorkService restTemplateWorkService;

    /**
     * Class constructor
     * @param restTemplateWorkService RestTemplateWorkService object
     */
    public TradeContractService(RestTemplateWorkService restTemplateWorkService) {
        this.restTemplateWorkService = restTemplateWorkService;
    }

    /**
     * This method returns a list of export collections by customer
     * @param customerPersonNumber the person number of the customer
     * @param requester User object
     * @see User
     * @see ExportCollection
     * @return a list of export collections by customer
     */
    public List<TradeContract> getCustomerImportCollections(String customerPersonNumber, UserInfo requester) {
        ControllerResponse response = restTemplateWorkService
                .findTradeContracts(CONTRACT_TYPE_CLI_REQUEST, parseQuery(customerPersonNumber, requester));
        return mapTradeContracts(response);
    }

    /**
     * Method class to parse data
     * @param customerPersonNumber the person number of the customer
     * @param requester User object
     * @see User
     * @see ExportCollectionQuery
     * @return ExportCollectionQuery object
     */
    private TradeContractsQuery parseQuery(String customerPersonNumber, UserInfo requester){
        TradeContractsQuery tradeContractsQuery = new TradeContractsQuery();
        tradeContractsQuery.setRequester(requester);
        tradeContractsQuery.setCustomerCode(customerPersonNumber);
        return tradeContractsQuery;
    }

    /**
     * This method returns a list of export collections
     * @param response the person number of the customer
     * @see User
     * @see ControllerResponse
     * @return a list of export collections
     */
    private List<TradeContract> mapTradeContracts(ControllerResponse response) {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, TradeContract.class);
        return mapper.convertValue(response.getEntity(), type);
    }
}
