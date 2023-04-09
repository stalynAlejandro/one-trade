package com.pagonxt.onetradefinance.work.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.indexing.SearchService;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksList;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksQuery;
import com.pagonxt.onetradefinance.work.configuration.ElasticsearchProperties;
import com.pagonxt.onetradefinance.work.security.CaseSecurityService;
import com.pagonxt.onetradefinance.work.serializer.HistoricTasksListQueryComposer;
import com.pagonxt.onetradefinance.work.serializer.PagoNxtHistoricTaskItemSerializer;
import com.pagonxt.onetradefinance.work.service.model.Task;
import com.pagonxt.onetradefinance.work.service.model.elastic.ElasticResponse;
import com.pagonxt.onetradefinance.work.service.model.elastic.Hit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * service class for historic tasks
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.serializer.HistoricTasksListQueryComposer
 * @see com.pagonxt.onetradefinance.work.serializer.PagoNxtHistoricTaskItemSerializer
 * @see com.pagonxt.onetradefinance.work.security.CaseSecurityService
 * @see com.flowable.indexing.SearchService
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@Service
public class HistoricTasksService {

    private static final TypeReference<ElasticResponse<Task>> TASK_ELASTIC_RESPONSE_TYPE_REFERENCE = new TypeReference<>() {
    };

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(HistoricTasksService.class);

    //class attributes
    private final ElasticsearchProperties elasticsearchProperties;
    private final HistoricTasksListQueryComposer historicTasksListQueryComposer;
    private final PagoNxtHistoricTaskItemSerializer pagoNxtHistoricTaskItemSerializer;
    private final CaseSecurityService caseSecurityService;
    private final SearchService searchService;
    private final ObjectMapper objectMapper;

    /**
     * constructor method
     *
     * @param elasticsearchProperties           : an ElasticsearchProperties object
     * @param historicTasksListQueryComposer    : a HistoricTasksListQueryComposer object
     * @param pagoNxtHistoricTaskItemSerializer : a PagoNxtHistoricTaskItemSerializer object
     * @param caseSecurityService               : a CaseSecurityService object
     * @param searchService                     : a SearchService object
     * @param objectMapper                      : a ObjectMapper object
     */
    public HistoricTasksService(ElasticsearchProperties elasticsearchProperties,
                                HistoricTasksListQueryComposer historicTasksListQueryComposer,
                                PagoNxtHistoricTaskItemSerializer pagoNxtHistoricTaskItemSerializer,
                                CaseSecurityService caseSecurityService,
                                @Autowired(required = false) SearchService searchService,
                                ObjectMapper objectMapper) {
        this.elasticsearchProperties = elasticsearchProperties;
        this.historicTasksListQueryComposer = historicTasksListQueryComposer;
        this.pagoNxtHistoricTaskItemSerializer = pagoNxtHistoricTaskItemSerializer;
        this.caseSecurityService = caseSecurityService;
        this.searchService = searchService;
        this.objectMapper = objectMapper;
    }

    /**
     * Method to get historic tasks
     * @param query : a HistoricTasksQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksList
     * @return a HistoricTasksList object
     */
    public HistoricTasksList getHistoricTasks(HistoricTasksQuery query) {
        LOG.debug("[getHistoricTasks](query: {})", query);
        String locale = query.getLocale();
        List<Task> taskList = List.of();
        long size = 0;
        if (searchService != null) {
            caseSecurityService.checkRead(query.getUserInfo(), query.getCode());
            String searchQuery = historicTasksListQueryComposer.compose(query);
            LOG.debug("[getHistoricTasks] elasticsearchQuery:\n{}", searchQuery);
            long start = System.currentTimeMillis();
            JsonNode resultNode = searchService.query("tasks", searchQuery);
            long end = System.currentTimeMillis();
            if (elasticsearchProperties.isLogResponseTimes()) {
                LOG.info("[ELASTIC][getHistoricTasks] Successful elasticsearch request took {} ms", end - start);
            }
            ElasticResponse<Task> elasticResponse;
            try {
                elasticResponse = objectMapper.readValue(objectMapper.treeAsTokens(resultNode),
                        TASK_ELASTIC_RESPONSE_TYPE_REFERENCE);
            } catch (IOException e) {
                throw new ServiceException("Error listing historic tasks", "getHistoricTasks", e);
            }
            taskList = elasticResponse.getHits().getHitList().stream()
                    .map(Hit::getSource).collect(Collectors.toList());
            size = elasticResponse.getSize();
        }
        HistoricTasksList result = new HistoricTasksList(
                taskList.stream().map(t -> pagoNxtHistoricTaskItemSerializer
                        .serialize(t, locale)).collect(Collectors.toList()),
                size);
        LOG.debug("[getHistoricTasks] result: {}", result);
        return result;
    }
}
