package com.pagonxt.onetradefinance.external.backend.service.cle;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvance;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionQuery;
import com.pagonxt.onetradefinance.integrations.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pagonxt.onetradefinance.integrations.constants.ExportCollectionConstants.COLLECTION_TYPE_ADVANCE;

/**
 * This Service class provides a way of a client to interact with some functionality in the application.
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService
 * @since jdk-11.0.13
 */
@Service
public class ExportCollectionAdvanceService {

    /**
     * Class attributes
     */
    private final RestTemplateWorkService restTemplateWorkService;

    /**
     * Class constructor
     * @param restTemplateWorkService RestTemplateWorkService object
     */
    public ExportCollectionAdvanceService(RestTemplateWorkService restTemplateWorkService) {
        this.restTemplateWorkService = restTemplateWorkService;
    }

    /**
     * This method returns a list of advances by customer
     * @param customerPersonNumber the person number of the customer
     * @param requester User object
     * @see com.pagonxt.onetradefinance.integrations.model.User
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvance
     * @return a list of advances by customer
     */
    public List<ExportCollectionAdvance> getCustomerExportCollectionAdvances(String customerPersonNumber,
                                                                             User requester) {
        ControllerResponse response = restTemplateWorkService
                .postExportCollections(parseQuery(customerPersonNumber, requester));
        return mapExportCollectionAdvances(response);
    }

    /**
     * Method class to parse data
     * @param customerPersonNumber the person number of the customer
     * @param requester User object
     * @see com.pagonxt.onetradefinance.integrations.model.User
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionQuery
     * @return ExportCollectionQuery object
     */
    private ExportCollectionQuery parseQuery(String customerPersonNumber, User requester){
        ExportCollectionQuery exportCollectionQuery = new ExportCollectionQuery();
        exportCollectionQuery.setRequester(requester);
        exportCollectionQuery.setCustomerPersonNumber(customerPersonNumber);
        exportCollectionQuery.setCollectionType(COLLECTION_TYPE_ADVANCE);
        return exportCollectionQuery;
    }

    /**
     * This method returns a list of advances
     * @param response the person number of the customer
     * @see com.pagonxt.onetradefinance.integrations.model.User
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return a list of advances
     */
    private List<ExportCollectionAdvance> mapExportCollectionAdvances(ControllerResponse response) {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, ExportCollectionAdvance.class);
        return mapper.convertValue(response.getEntity(), type);
    }
}
