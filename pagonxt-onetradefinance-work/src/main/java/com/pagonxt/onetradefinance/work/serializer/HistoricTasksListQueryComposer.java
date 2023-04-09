package com.pagonxt.onetradefinance.work.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.pagonxt.onetradefinance.work.common.FilterConstants.OPERATION_CODE;
import static com.pagonxt.onetradefinance.work.serializer.SortField.ROOT_SCOPE;

/**
 * Service class to provide some methods to generate queries
 * In this case, they are some methods to get a list of historic tasks
 * @author -
 * @version jdk-11.0.13
 * @see  ElasticQueryComposer
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@Service
public class HistoricTasksListQueryComposer extends ElasticQueryComposer {

    /**
     * constructor method
     * @param dateFormatProperties : date format properties
     * @param objectMapper : provides functionality for reading and writing JSON
     */
    public HistoricTasksListQueryComposer(DateFormatProperties dateFormatProperties,
                                          ObjectMapper objectMapper) {
        super(dateFormatProperties, objectMapper, Arrays.asList(
                SortField.ofDate("startDate", "createTime"),
                SortField.ofDate("endDate", "endTime")
        ));
    }

    /**
     * class method for queries
     * @param query : a HistoricTasksQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksQuery
     * @return  a String object
     */
    public String compose(HistoricTasksQuery query) {
        List<String> conditions = new ArrayList<>();
        conditions.add(getVariableHasExactTextCondition(OPERATION_CODE, ROOT_SCOPE, query.getCode()));
        conditions.add(getPropertyExistsCondition("endTime"));
        // Default sort by startDate desc
        String sortField = StringUtils.hasText(query.getSortField()) ? query.getSortField() : "startDate";
        Integer sortOrder = query.getSortOrder() == 0 ? -1 : query.getSortOrder();

        return composeQuery(conditions,
                query.getFromPage(), query.getPageSize(),
                sortField, sortOrder);
    }
}
