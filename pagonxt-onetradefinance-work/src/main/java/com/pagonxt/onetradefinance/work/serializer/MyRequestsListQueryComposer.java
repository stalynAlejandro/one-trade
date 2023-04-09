package com.pagonxt.onetradefinance.work.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery;
import com.pagonxt.onetradefinance.integrations.service.OfficeService;
import com.pagonxt.onetradefinance.work.utils.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.EVENT;
import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.PRODUCT;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.*;
import static com.pagonxt.onetradefinance.work.common.FilterConstants.*;
import static com.pagonxt.onetradefinance.work.utils.TaskUtils.generateVarName;

/**
 * Service class to provide some methods to generate queries
 * In this case, they are some methods to get a list of requests
 * @author -
 * @version jdk-11.0.13
 * @see  ElasticQueryComposer
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see com.pagonxt.onetradefinance.integrations.service.OfficeService
 * @since jdk-11.0.13
 */
@Service
public class MyRequestsListQueryComposer extends ElasticQueryComposer {

    //class attribute
    private final OfficeService officeService;

    private final String timeZone;

    /**
     * constructor method
     *
     * @param dateFormatProperties : date format properties
     * @param officeService : an OfficeService object
     * @param objectMapper : provides functionality for reading and writing JSON
     *
     */
    public MyRequestsListQueryComposer(DateFormatProperties dateFormatProperties, OfficeService officeService, ObjectMapper objectMapper) {
        super(dateFormatProperties, objectMapper, Arrays.asList(
                SortField.ofString("userId", generateVarName(OPERATION_CUSTOMER_ID)),
                SortField.ofString("clientId", generateVarName(OPERATION_CUSTOMER_ID)),
                SortField.ofString("mercuryRef", generateVarName(OPERATION_CONTRACT_REFERENCE)),
                SortField.ofString("operationId", generateVarName(OPERATION_CODE)),
                SortField.ofDate("issuanceDate", generateVarName(OPERATION_CREATION_DATE)),
                SortField.ofString("product", generateVarName(PRODUCT)),
                SortField.ofString("event", generateVarName(EVENT)),
                SortField.ofString("task", "variables.task"),
                SortField.ofString("priority", generateVarName(OPERATION_PRIORITY)),
                SortField.ofString("status", generateVarName(OPERATION_DISPLAYED_STATUS)),
                SortField.ofDouble("amount", generateVarName(OPERATION_AMOUNT)),
                SortField.ofString("currency", generateVarName(OPERATION_CURRENCY)),
                SortField.ofDate("requestedDate", generateVarName(OPERATION_CREATION_DATE)),
                SortField.ofString("contractReference", generateVarName(OPERATION_CONTRACT_REFERENCE)),
                SortField.ofString("office", generateVarName(OPERATION_OFFICE)),
                SortField.ofString("resolution", generateVarName(OPERATION_RESOLUTION)),
                SortField.ofString("client", generateVarName(OPERATION_CUSTOMER_FULL_NAME)),
                SortField.ofString("requesterName", generateVarName(OPERATION_REQUESTER_DISPLAYED_NAME))
        ));
        this.officeService = officeService;
        this.timeZone = dateFormatProperties.getTimeZone();
    }

    /**
     * class method for queries
     *
     * @param query : a MyRequestsQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery
     *
     * @return  a String object
     */
    public String compose(MyRequestsQuery query) {
        List<String> conditions = new ArrayList<>();

        // Split into sub-methods to reduce complexity
        conditions.addAll(getGeneralConditions(query));
        conditions.addAll(getQueryConditions(query));

        if (!USER_TYPE_CUSTOMER.equals(query.getRequester().getUserType())) {
            conditions.addAll(getNonCustomerQueryConditions(query));
        }

        // Default sort by operationId desc
        String sortField = StringUtils.hasText(query.getSortField()) ? query
                .getSortField() : FieldConstants.OPERATION_ID;
        Integer sortOrder = query.getSortOrder() == 0 ? -1 : query.getSortOrder();

        return composeQuery(conditions,
                query.getFromPage(), query.getPageSize(),
                sortField, sortOrder);
    }

    /**
     * class method for queries
     * Conditions common to all requests
     *
     * @param query : a MyRequestsQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery
     *
     * @return  a list with the query result
     */
    private List<String> getGeneralConditions(MyRequestsQuery query) {
        List<String> result = new ArrayList<>();

        // Discard cancelled draft cases
        result.add(getVariableDoesNotExistCondition(FieldConstants.REGISTRATION_CANCELLED));

        // For middle office users, allow only cases from the user middle office
        if (USER_TYPE_MIDDLE_OFFICE.equals(query.getRequester().getUserType())) {
            String middleOffice = officeService.getRequesterMiddleOffice(query.getRequester());
            result.add(getVariableHasExactTextCondition(OPERATION_MIDDLE_OFFICE, middleOffice));
        }
        // For office users, allow only cases from the user office
        if (USER_TYPE_OFFICE.equals(query.getRequester().getUserType())) {
            String office = officeService.getRequesterOffice(query.getRequester());
            result.add(getVariableHasExactTextCondition(OPERATION_OFFICE, office));
        }
        // For client users, allow only user related cases
        if (USER_TYPE_CUSTOMER.equals(query.getRequester().getUserType())) {
            result.add(getVariableHasExactTextCondition(OPERATION_CUSTOMER_ID, query.getRequester().getUserId()));
        }
        return result;
    }

    /**
     * class method for queries
     * Conditions based on query values
     *
     * @param query : a MyRequestsQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery
     *
     * @return  a list with the query result
     */
    private List<String> getQueryConditions(MyRequestsQuery query) {
        List<String> result = new ArrayList<>();

        if (StringUtils.hasText(query.getCode())) {
            result.add(getVariableContainsTextCondition(OPERATION_CODE, query.getCode().trim()));
        }

        if (query.getProductId() != null && !query.getProductId().isEmpty()) {
            List<String> productIdConditions = query.getProductId().stream()
                    .map(productId -> getVariableHasExactTextCondition(PRODUCT, productId))
                    .collect(Collectors.toList());
            result.add(getOrCondition(productIdConditions));
        }

        if (query.getEventId() != null && !query.getEventId().isEmpty()) {
            List<String> eventIdConditions = query.getEventId().stream()
                    .map(eventId -> getVariableHasExactTextCondition(EVENT, eventId))
                    .collect(Collectors.toList());
            result.add(getOrCondition(eventIdConditions));
        }

        if (query.getPriority() != null && !query.getPriority().isEmpty()) {
            List<String> priorityConditions = query.getPriority().stream()
                    .map(priority -> getVariableHasExactTextCondition(OPERATION_PRIORITY, priority))
                    .collect(Collectors.toList());
            result.add(getOrCondition(priorityConditions));
        }

        if (query.getCurrency() != null && !query.getCurrency().isEmpty()) {
            List<String> currencyConditions = query.getCurrency().stream()
                    .map(currency -> getVariableHasExactTextCondition(OPERATION_CURRENCY, currency))
                    .collect(Collectors.toList());
            result.add(getOrCondition(currencyConditions));
        }

        if (query.getFromAmount() != null) {
            result.add(getVariableGTECondition(OPERATION_AMOUNT, query.getFromAmount()));
        }
        if (query.getToAmount() != null) {
            result.add(getVariableLTECondition(OPERATION_AMOUNT, query.getToAmount()));
        }

        // Note: cases in 'draft' state won't have an operationCreationDate variable,
        // so searching by date range will exclude them
        if (query.getFromDate() != null) {
            result.add(getVariableGTECondition(OPERATION_CREATION_DATE,
                    DateUtils.getStartOfDay(query.getFromDate(), timeZone)));
        }
        if (query.getToDate() != null) {
            result.add(getVariableLTECondition(OPERATION_CREATION_DATE,
                    DateUtils.getEndOfDay(query.getToDate(), timeZone)));
        }

        return result;
    }

    /**
     * class method for queries
     * Conditions not available for customer requesters
     *
     * @param query : a MyRequestsQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery
     *
     * @return  a list with the query result
     */
    private List<String> getNonCustomerQueryConditions(MyRequestsQuery query) {
        List<String> result = new ArrayList<>();
        if (StringUtils.hasText(query.getCustomerName())) {
            result.add(getVariableContainsTextCondition(OPERATION_CUSTOMER_FULL_NAME, query.getCustomerName()));
        }
        if (StringUtils.hasText(query.getCustomerPersonNumber())) {
            result.add(getVariableContainsTextCondition(OPERATION_CUSTOMER_CODE, query.getCustomerPersonNumber()));
        }
        if (StringUtils.hasText(query.getCustomerTaxId())) {
            result.add(getVariableContainsTextCondition(OPERATION_CUSTOMER_ID, query.getCustomerTaxId()));
        }

        return result;
    }
}
