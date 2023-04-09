package com.pagonxt.onetradefinance.external.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.Filters;
import com.pagonxt.onetradefinance.external.backend.api.model.FiltersDefinition;
import com.pagonxt.onetradefinance.external.backend.api.model.ListRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksList;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pagonxt.onetradefinance.external.backend.utils.FilterUtils.getDouble;
import static com.pagonxt.onetradefinance.external.backend.utils.FilterUtils.getJSONDate;
import static com.pagonxt.onetradefinance.external.backend.utils.FilterUtils.getString;
import static com.pagonxt.onetradefinance.external.backend.utils.FilterUtils.getStringList;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.USER_TYPE_BACKOFFICE;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.USER_TYPE_CUSTOMER;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.USER_TYPE_MIDDLE_OFFICE;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.USER_TYPE_OFFICE;

/**
 * This Service class provides a way of a client to interact with some functionality in the application.
 * @author -
 * @version jdk-11.0.13
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService
 * @since jdk-11.0.13
 */
@Service
public class MyTasksService {

    private static final String FILTER_MY_TASKS_FILENAME = "filters/my-tasks.json";
    private static final String FILTER_MY_TASKS_CUSTOMER_FILENAME = "filters/my-tasks-customer.json";
    private static final Map<String, String> FILTER_DEFINITIONS_BY_USER_TYPE = Map.of(
            USER_TYPE_OFFICE, FILTER_MY_TASKS_FILENAME,
            USER_TYPE_MIDDLE_OFFICE, FILTER_MY_TASKS_FILENAME,
            USER_TYPE_BACKOFFICE, FILTER_MY_TASKS_FILENAME,
            USER_TYPE_CUSTOMER, FILTER_MY_TASKS_CUSTOMER_FILENAME
    );

    private static final List<String> VALID_SCOPES = List.of("mine", "all");
    private static final String DEFAULT_SCOPE = "mine";

    private final Map<String, FiltersDefinition> filtersByUserType = new HashMap<>();

    /**
     * Class attributes
     * ObjectMapper provides functionality for reading and writing JSON,
     * either to and from basic POJOs (Plain Old Java Objects),
     * or to and from a general-purpose JSON Tree Model
     */
    private final RestTemplateWorkService restTemplateWorkService;
    private final ObjectMapper objectMapper;

    /**
     * Class constructor
     * @param restTemplateWorkService RestTemplateWorkService object
     * @param objectMapper ObjectMapper object
     */
    public MyTasksService(RestTemplateWorkService restTemplateWorkService, ObjectMapper objectMapper) {
        this.restTemplateWorkService = restTemplateWorkService;
        this.objectMapper = objectMapper;
    }

    /**
     * This method return a list of tasks by user
     * @param request ListRequest object
     * @param scope the scope
     * @param requester User object with the requester
     * @see com.pagonxt.onetradefinance.external.backend.api.model.ListRequest
     * @see com.pagonxt.onetradefinance.integrations.model.User
     * @see com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksList
     * @return a list of tasks by user
     */
    public MyTasksList getMyTasks(ListRequest request, String scope, User requester) {
        String requestScope =
                (scope == null || !VALID_SCOPES.contains(scope.toLowerCase())) ? DEFAULT_SCOPE : scope.toLowerCase();
        return restTemplateWorkService.postMyTasks(parseTaskQuery(request, requestScope, requester));
    }

    /**
     * This method gets filters to select which tasks should be printed
     * @param userType string with the user type
     * @see com.pagonxt.onetradefinance.external.backend.api.model.FiltersDefinition
     * @return FiltersDefinition object
     */
    public FiltersDefinition getFilters(String userType) {
        if (!filtersByUserType.containsKey(userType)) {
            String resourceName = FILTER_DEFINITIONS_BY_USER_TYPE.get(userType);
            try (InputStream resourceStream = this.getClass().getClassLoader()
                    .getResourceAsStream(resourceName)) {
                filtersByUserType.put(userType, objectMapper.readValue(resourceStream, new TypeReference<>() {}));
            } catch (Exception e) {
                throw new ServiceException(String.format("Error getting my tasks filters for user type %s",
                        userType), "getFiltersError", e);
            }
        }
        return filtersByUserType.get(userType);
    }

    /**
     * Class method to parse data
     * @param request ListRequest object
     * @param scope the scope
     * @param requester User object with the requester
     * @see com.pagonxt.onetradefinance.external.backend.api.model.ListRequest
     * @see com.pagonxt.onetradefinance.integrations.model.User
     * @see com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery
     * @return MyTasksQuery object
     */
    private MyTasksQuery parseTaskQuery(ListRequest request, String scope, User requester) {
        MyTasksQuery result = new MyTasksQuery();
        result.setScope(scope);
        result.setRequester(requester);
        Filters filters = request.getFilters();
        result.setCode(getString(filters, "code"));
        result.setCustomerName(getString(filters, "customerName"));
        result.setCustomerPersonNumber(getString(filters, "customerPersonNumber"));
        result.setCustomerTaxId(getString(filters, "customerTaxId"));
        result.setRequesterName(getString(filters, "requesterName"));
        result.setOwnerName(getString(filters, "ownerName"));
        result.setProductId(getStringList(filters, "productId"));
        result.setEventId(getStringList(filters, "eventId"));
        result.setTaskId(getStringList(filters, "taskId"));
        result.setFromDate(getJSONDate(filters, "fromDate"));
        result.setToDate(getJSONDate(filters, "toDate"));
        result.setPriority(getStringList(filters, "priority"));
        result.setStatus(getStringList(filters, "status"));
        result.setFromAmount(getDouble(filters, "fromAmount"));
        result.setToAmount(getDouble(filters, "toAmount"));
        result.setCurrency(getStringList(filters, "currency"));
        result.setSortField(request.getSortField());
        result.setSortOrder(request.getSortOrder());
        result.setFromPage(request.getFromPage());
        result.setPageSize(request.getPageSize());
        return result;
    }

}
