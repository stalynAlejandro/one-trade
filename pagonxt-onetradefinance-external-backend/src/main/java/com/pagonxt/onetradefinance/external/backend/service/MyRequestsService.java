package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.external.backend.api.model.Filters;
import com.pagonxt.onetradefinance.external.backend.api.model.ListRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsList;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery;
import org.springframework.stereotype.Service;

import static com.pagonxt.onetradefinance.external.backend.utils.FilterUtils.*;

/**
 * This Service class provides a way of a client to interact with some functionality in the application.
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService
 * @since jdk-11.0.13
 */
@Service
public class MyRequestsService {

    /**
     * Class attributes
     */
    private final RestTemplateWorkService restTemplateWorkService;

    /**
     * Class constructor
     * @param restTemplateWorkService RestTemplateWorkService object
     */
    public MyRequestsService(RestTemplateWorkService restTemplateWorkService) {
        this.restTemplateWorkService = restTemplateWorkService;
    }

    /**
     * Method to return a list of request by user
     * @param request a list of request
     * @param requester the requester
     * @see com.pagonxt.onetradefinance.external.backend.api.model.ListRequest
     * @see com.pagonxt.onetradefinance.integrations.model.User
     * @see com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsList
     * @return a list of requests by user
     */
    public MyRequestsList getMyRequests(ListRequest request, User requester) {
        return restTemplateWorkService.postMyRequests(parseRequest(request, requester));
    }

    /**
     * method to parse the data of the requests
     * @param request a list of request
     * @param requester the requester
     * @see com.pagonxt.onetradefinance.external.backend.api.model.ListRequest
     * @see com.pagonxt.onetradefinance.integrations.model.User
     * @see com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery
     * @return HistoryTasksQuery object
     */
    private MyRequestsQuery parseRequest(ListRequest request, User requester) {
       MyRequestsQuery result = new MyRequestsQuery();
        result.setRequester(requester);
        Filters filters = request.getFilters();
        result.setCode(getString(filters, "code"));
        result.setProductId(getStringList(filters, "productId"));
        result.setEventId(getStringList(filters, "eventId"));
        result.setPriority(getStringList(filters, "priority"));
        result.setFromDate(getJSONDate(filters, "fromDate"));
        result.setToDate(getJSONDate(filters, "toDate"));
        result.setCurrency(getStringList(filters, "currency"));
        result.setFromAmount(getDouble(filters, "fromAmount"));
        result.setToAmount(getDouble(filters, "toAmount"));
        result.setCustomerName(getString(filters, "customerName"));
        result.setCustomerPersonNumber(getString(filters, "customerPersonNumber"));
        result.setCustomerTaxId(getString(filters, "customerTaxId"));
        result.setSortField(request.getSortField());
        result.setSortOrder(request.getSortOrder());
        result.setFromPage(request.getFromPage());
        result.setPageSize(request.getPageSize());
        return result;
    }
}
