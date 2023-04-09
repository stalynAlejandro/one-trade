package com.pagonxt.onetradefinance.work.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery;
import com.pagonxt.onetradefinance.integrations.service.OfficeService;
import com.pagonxt.onetradefinance.work.utils.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.*;
import static com.pagonxt.onetradefinance.work.common.FilterConstants.OPERATION_CODE;
import static com.pagonxt.onetradefinance.work.common.FilterConstants.OPERATION_MIDDLE_OFFICE;
import static com.pagonxt.onetradefinance.work.common.FilterConstants.OPERATION_OFFICE;
import static com.pagonxt.onetradefinance.work.common.FilterConstants.*;
import static com.pagonxt.onetradefinance.work.serializer.SortField.ROOT_SCOPE;
import static com.pagonxt.onetradefinance.work.utils.TaskUtils.generateVarName;

/**
 * Service class to provide some methods to generate queries
 * In this case, they are some methods to get a list of tasks
 * @author -
 * @version jdk-11.0.13
 * @see  ElasticQueryComposer
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see com.pagonxt.onetradefinance.integrations.service.OfficeService
 * @since jdk-11.0.13
 */

@Service
public class MyTasksListQueryComposer extends ElasticQueryComposer {

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
    public MyTasksListQueryComposer(DateFormatProperties dateFormatProperties, OfficeService officeService, ObjectMapper objectMapper) {
        super(dateFormatProperties, objectMapper, Arrays.asList(
                SortField.ofString(FIELD_OPERATION_ID, generateVarName(OPERATION_CODE), ROOT_SCOPE),
                SortField.ofString(FIELD_MERCURY_REF, generateVarName(OPERATION_CONTRACT_REFERENCE), ROOT_SCOPE),
                SortField.ofDate(FIELD_ISSUANCE_DATE, generateVarName(OPERATION_CREATION_DATE), ROOT_SCOPE),
                SortField.ofString(FIELD_PRODUCT, generateVarName(PRODUCT), ROOT_SCOPE),
                SortField.ofString(FIELD_EVENT, generateVarName(EVENT), ROOT_SCOPE),
                SortField.ofString(FIELD_CLIENT, generateVarName(OPERATION_CUSTOMER_FULL_NAME), ROOT_SCOPE),
                SortField.ofString(FIELD_TASK, generateVarName(EXTERNAL_TASK_TYPE), null),
                SortField.ofString(FIELD_PRIORITY, generateVarName(OPERATION_PRIORITY), ROOT_SCOPE),
                SortField.ofString(FIELD_STATUS, generateVarName(OPERATION_DISPLAYED_STATUS), ROOT_SCOPE),
                SortField.ofString(FIELD_REQUESTER_NAME,
                        generateVarName(OPERATION_REQUESTER_DISPLAYED_NAME), ROOT_SCOPE)
        ));
        this.officeService = officeService;
        this.timeZone = dateFormatProperties.getTimeZone();
    }

    /**
     * class method for queries
     *
     * @param query : a MyTasksQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery
     *
     * @return  a String object
     */
    public String compose(MyTasksQuery query) {
        List<String> conditions = new ArrayList<>();

        // Split into sub-methods to reduce complexity
        conditions.addAll(getGeneralConditions(query));
        conditions.addAll(getComboQueryConditions(query));
        conditions.addAll(getOtherQueryConditions(query));
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
     * @param query : a MyTasksQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery
     *
     * @return  a list with the query result
     */
    private List<String> getGeneralConditions(MyTasksQuery query) {
        List<String> result = new ArrayList<>();

        // Discard cancelled draft cases
        result.add(getVariableDoesNotExistCondition(FieldConstants.REGISTRATION_CANCELLED, ROOT_SCOPE));
        // Discard completed tasks
        result.add(getPropertyDoesNotExistCondition(FieldConstants.END_TIME));
        // Get only external tasks
        result.add(getVariableHasExactTextCondition(FieldConstants.IS_EXTERNAL_TASK, Boolean.TRUE.toString()));

        // For middle office users, allow only cases from the user middle office
        if (USER_TYPE_MIDDLE_OFFICE.equals(query.getRequester().getUserType())) {
            String middleOffice = officeService.getRequesterMiddleOffice(query.getRequester());
            result.add(getVariableHasExactTextCondition(OPERATION_MIDDLE_OFFICE, ROOT_SCOPE, middleOffice));
        }
        // For office users, allow only cases from the user office
        if (USER_TYPE_OFFICE.equals(query.getRequester().getUserType())) {
            String office = officeService.getRequesterOffice(query.getRequester());
            result.add(getVariableHasExactTextCondition(OPERATION_OFFICE, ROOT_SCOPE, office));
        }
        // For customer users, allow only user related cases
        if (USER_TYPE_CUSTOMER.equals(query.getRequester().getUserType())) {
            result.add(getVariableHasExactTextCondition(OPERATION_CUSTOMER_ID, ROOT_SCOPE, query
                    .getRequester().getUserId()));
        }
        // Scope filtering, default is 'mine'
        if (!StringUtils.hasText(query.getScope()) || "mine".equals(query.getScope())) {
            result.add(getVariableHasExactTextCondition(OPERATION_REQUESTER_ID, query.getRequester().getUserId()));
        }

        return result;
    }

    /**
     * class method for queries
     * Conditions based on query combo values
     *
     * @param query : a MyTasksQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery
     *
     * @return  a list with the query result
     */
    private List<String> getComboQueryConditions(MyTasksQuery query) {
        List<String> result = new ArrayList<>();

        if (query.getProductId() != null && !query.getProductId().isEmpty()) {
            List<String> productIdConditions = query.getProductId().stream()
                    .map(productId -> getVariableHasExactTextCondition(PRODUCT, ROOT_SCOPE, productId))
                    .collect(Collectors.toList());
            result.add(getOrCondition(productIdConditions));
        }

        if (query.getEventId() != null && !query.getEventId().isEmpty()) {
            List<String> eventIdConditions = query.getEventId().stream()
                    .map(eventId -> getVariableHasExactTextCondition(EVENT, ROOT_SCOPE, eventId))
                    .collect(Collectors.toList());
            result.add(getOrCondition(eventIdConditions));
        }

        if (query.getTaskId() != null && !query.getTaskId().isEmpty()) {
            List<String> taskIdConditions = query.getTaskId().stream()
                    .map(taskId -> getVariableHasExactTextCondition(EXTERNAL_TASK_TYPE, taskId))
                    .collect(Collectors.toList());
            result.add(getOrCondition(taskIdConditions));
        }

        if (query.getPriority() != null && !query.getPriority().isEmpty()) {
            List<String> priorityConditions = query.getPriority().stream()
                    .map(priority -> getVariableHasExactTextCondition(OPERATION_PRIORITY, ROOT_SCOPE, priority))
                    .collect(Collectors.toList());
            result.add(getOrCondition(priorityConditions));
        }

        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            List<String> statusConditions = query.getStatus().stream()
                    .map(status -> "DRAFT".equals(status) ?
                            getVariableDoesNotExistCondition(OPERATION_DISPLAYED_STATUS, ROOT_SCOPE) :
                            getVariableHasExactTextCondition(OPERATION_DISPLAYED_STATUS, ROOT_SCOPE, status))
                    .collect(Collectors.toList());
            result.add(getOrCondition(statusConditions));
        }

        if (query.getCurrency() != null && !query.getCurrency().isEmpty()) {
            List<String> currencyConditions = query.getCurrency().stream()
                    .map(currency -> getVariableHasExactTextCondition(OPERATION_CURRENCY, ROOT_SCOPE, currency))
                    .collect(Collectors.toList());
            result.add(getOrCondition(currencyConditions));
        }

        return result;
    }

    /**
     * class method for queries
     * Conditions based on other query values
     *
     * @param query : a MyTasksQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery
     *
     * @return  a list with the query result
     */
    private List<String> getOtherQueryConditions(MyTasksQuery query) {
        List<String> result = new ArrayList<>();

        if (StringUtils.hasText(query.getCode())) {
            result.add(getVariableContainsTextCondition(OPERATION_CODE, ROOT_SCOPE, query.getCode().trim()));
        }

        if (query.getFromDate() != null) {
            result.add(getVariableGTECondition(OPERATION_CREATION_DATE, ROOT_SCOPE,
                    DateUtils.getStartOfDay(query.getFromDate(), timeZone)));
        }
        if (query.getToDate() != null) {
            result.add(getVariableLTECondition(OPERATION_CREATION_DATE, ROOT_SCOPE,
                    DateUtils.getEndOfDay(query.getToDate(), timeZone)));
        }

        if (query.getFromAmount() != null) {
            result.add(getVariableGTECondition(OPERATION_AMOUNT, ROOT_SCOPE, query.getFromAmount()));
        }
        if (query.getToAmount() != null) {
            result.add(getVariableLTECondition(OPERATION_AMOUNT, ROOT_SCOPE, query.getToAmount()));
        }

        return result;
    }

    /**
     * class method for queries
     * Conditions not available for customer requesters
     *
     * @param query : a MyTasksQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery
     *
     * @return  a list with the query result
     */
    private List<String> getNonCustomerQueryConditions(MyTasksQuery query) {
        List<String> result = new ArrayList<>();

        if (StringUtils.hasText(query.getCustomerName())) {
            result.add(getVariableContainsTextCondition(OPERATION_CUSTOMER_FULL_NAME, ROOT_SCOPE, query
                    .getCustomerName()));
        }
        if (StringUtils.hasText(query.getCustomerPersonNumber())) {
            result.add(getVariableContainsTextCondition(OPERATION_CUSTOMER_CODE, ROOT_SCOPE, query
                    .getCustomerPersonNumber()));
        }
        if (StringUtils.hasText(query.getCustomerTaxId())) {
            result.add(getVariableContainsTextCondition(OPERATION_CUSTOMER_ID, ROOT_SCOPE, query.getCustomerTaxId()));
        }
        if (StringUtils.hasText(query.getRequesterName())) {
            result.add(getVariableContainsTextCondition(OPERATION_REQUESTER_DISPLAYED_NAME, ROOT_SCOPE, query
                    .getRequesterName()));
        }
        // TODO [PGNXTOTF-177] confirmar el origen de este dato, que no será el initiator
        if (StringUtils.hasText(query.getOwnerName())) {
            result.add(getVariableContainsTextCondition(FieldConstants.INITIATOR, ROOT_SCOPE, query.getOwnerName()));
        }

        return result;
    }
}
