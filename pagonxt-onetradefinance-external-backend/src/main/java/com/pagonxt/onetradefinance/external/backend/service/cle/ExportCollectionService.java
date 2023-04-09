package com.pagonxt.onetradefinance.external.backend.service.cle;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollection;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionQuery;
import com.pagonxt.onetradefinance.integrations.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pagonxt.onetradefinance.integrations.constants.ExportCollectionConstants.COLLECTION_TYPE_REQUEST;

/**
 * This Service class provides a way of a client to interact with some functionality in the application.
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService
 * @since jdk-11.0.13
 */
@Service
public class ExportCollectionService {

    /**
     * Class attributes
     */
    private final RestTemplateWorkService restTemplateWorkService;

    /**
     * Class constructor
     * @param restTemplateWorkService RestTemplateWorkService object
     */
    public ExportCollectionService(RestTemplateWorkService restTemplateWorkService) {
        this.restTemplateWorkService = restTemplateWorkService;
    }

    /**
     * This method returns a list of export collections by customer
     * @param customerPersonNumber the person number of the customer
     * @param requester User object
     * @see com.pagonxt.onetradefinance.integrations.model.User
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollection
     * @return a list of export collections by customer
     */
    public List<ExportCollection> getCustomerExportCollections(String customerPersonNumber, User requester) {
        ControllerResponse response = restTemplateWorkService
                .postExportCollections(parseQuery(customerPersonNumber, requester));
        return mapExportCollections(response);
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
        exportCollectionQuery.setCollectionType(COLLECTION_TYPE_REQUEST);
        return exportCollectionQuery;
    }

    /**
     * This method returns a list of export collections
     * @param response the person number of the customer
     * @see com.pagonxt.onetradefinance.integrations.model.User
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return a list of export collections
     */
    private List<ExportCollection> mapExportCollections(ControllerResponse response) {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, ExportCollection.class);
        return mapper.convertValue(response.getEntity(), type);
    }
}
