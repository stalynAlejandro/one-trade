package com.pagonxt.onetradefinance.work.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.indexing.SearchService;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksList;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery;
import com.pagonxt.onetradefinance.work.configuration.ElasticsearchProperties;
import com.pagonxt.onetradefinance.work.serializer.MyTasksListQueryComposer;
import com.pagonxt.onetradefinance.work.serializer.PagoNxtTaskItemSerializer;
import com.pagonxt.onetradefinance.work.service.model.Task;
import com.pagonxt.onetradefinance.work.service.model.elastic.ElasticResponse;
import com.pagonxt.onetradefinance.work.service.model.elastic.Hit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * service class for tasks
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.serializer.MyTasksListQueryComposer
 * @see com.pagonxt.onetradefinance.work.serializer.PagoNxtTaskItemSerializer
 * @see com.flowable.indexing.SearchService
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@Service
public class MyTasksService {

    private static final TypeReference<ElasticResponse<Task>> TASK_ELASTIC_RESPONSE_TYPE_REFERENCE =
            new TypeReference<>() {
    };

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(MyTasksService.class);

    private final ElasticsearchProperties elasticsearchProperties;
    private final MyTasksListQueryComposer myTasksListQueryComposer;
    private final PagoNxtTaskItemSerializer pagoNxtTaskItemSerializer;
    private final SearchService searchService;
    private final ObjectMapper objectMapper;

    /**
     * constructor method
     *
     * @param elasticsearchProperties   : an ElasticsearchProperties object
     * @param myTasksListQueryComposer  : a MyTasksListQueryComposer object
     * @param pagoNxtTaskItemSerializer : a PagoNxtTaskItemSerializer object
     * @param searchService             : a SearchService object
     * @param objectMapper              : a ObjectMapper object
     */
    public MyTasksService(ElasticsearchProperties elasticsearchProperties,
                          MyTasksListQueryComposer myTasksListQueryComposer,
                          PagoNxtTaskItemSerializer pagoNxtTaskItemSerializer,
                          @Autowired(required = false) SearchService searchService,
                          ObjectMapper objectMapper) {
        this.elasticsearchProperties = elasticsearchProperties;
        this.myTasksListQueryComposer = myTasksListQueryComposer;
        this.pagoNxtTaskItemSerializer = pagoNxtTaskItemSerializer;
        this.searchService = searchService;
        this.objectMapper = objectMapper;
    }

    /**
     * Method to get the user tasks
     * @param query : a MyTasksQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery
     * @see com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksList
     * @return a MyTasksList object
     */
    public MyTasksList getMyTasks(MyTasksQuery query) {
        LOG.debug("[getMyTasks](query: {})", query);
        try {
            List<Task> taskList = List.of();
            long size = 0;
            if (searchService != null) {
                String searchQuery = myTasksListQueryComposer.compose(query);
                LOG.debug("[getMyTasks] elasticsearchQuery:\n{}", searchQuery);
                long start = System.currentTimeMillis();
                JsonNode resultNode = searchService.query("tasks", searchQuery);
                long end = System.currentTimeMillis();
                if (elasticsearchProperties.isLogResponseTimes()) {
                    LOG.info("[ELASTIC][getMyTasks][{}] Successful elasticsearch request took {} ms", query.getScope(), end - start);
                }
                ElasticResponse<Task> elasticResponse = objectMapper.readValue(objectMapper.treeAsTokens(resultNode),
                        TASK_ELASTIC_RESPONSE_TYPE_REFERENCE);
                taskList = elasticResponse.getHits().getHitList()
                        .stream().map(Hit::getSource).collect(Collectors.toList());
                size = elasticResponse.getSize();
            }
            MyTasksList result = new MyTasksList(
                    taskList.stream().map(pagoNxtTaskItemSerializer::serialize).collect(Collectors.toList()),
                    size);
            LOG.debug("[getMyTasks] result: {}", result);
            return result;
        } catch (Exception e) {
            throw new ServiceException("Error listing my tasks", "getMyTasks", e);
        }
    }
}
