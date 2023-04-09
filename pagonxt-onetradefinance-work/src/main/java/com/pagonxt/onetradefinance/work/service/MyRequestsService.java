package com.pagonxt.onetradefinance.work.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.indexing.SearchService;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsList;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery;
import com.pagonxt.onetradefinance.work.configuration.ElasticsearchProperties;
import com.pagonxt.onetradefinance.work.serializer.MyRequestsListQueryComposer;
import com.pagonxt.onetradefinance.work.serializer.PagoNxtRequestItemSerializer;
import com.pagonxt.onetradefinance.work.service.model.Case;
import com.pagonxt.onetradefinance.work.service.model.elastic.ElasticResponse;
import com.pagonxt.onetradefinance.work.service.model.elastic.Hit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * service class for requests
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.serializer.MyRequestsListQueryComposer
 * @see com.pagonxt.onetradefinance.work.serializer.PagoNxtRequestItemSerializer
 * @see com.flowable.indexing.SearchService
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@Service
public class MyRequestsService {

    private static final TypeReference<ElasticResponse<Case>> CASE_ELASTIC_RESPONSE_TYPE_REFERENCE =
            new TypeReference<>() {
    };

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(MyRequestsService.class);

    //class attributes
    private final ElasticsearchProperties elasticsearchProperties;
    private final SearchService searchService;
    private final MyRequestsListQueryComposer myRequestsListQueryComposer;
    private final PagoNxtRequestItemSerializer pagoNxtRequestItemSerializer;
    private final ObjectMapper objectMapper;

    /**
     * constructor method
     *
     * @param elasticsearchProperties      : an ElasticsearchProperties object
     * @param myRequestsListQueryComposer  : a MyRequestsListQueryComposer object
     * @param pagoNxtRequestItemSerializer : a PagoNxtRequestItemSerializer object
     * @param searchService                : a SearchService object
     * @param objectMapper                 : a ObjectMapper object
     */
    public MyRequestsService(ElasticsearchProperties elasticsearchProperties,
                             MyRequestsListQueryComposer myRequestsListQueryComposer,
                             PagoNxtRequestItemSerializer pagoNxtRequestItemSerializer,
                             @Autowired(required = false) SearchService searchService,
                             ObjectMapper objectMapper) {
        this.elasticsearchProperties = elasticsearchProperties;
        this.myRequestsListQueryComposer = myRequestsListQueryComposer;
        this.pagoNxtRequestItemSerializer = pagoNxtRequestItemSerializer;
        this.searchService = searchService;
        this.objectMapper = objectMapper;
    }

    /**
     * Method to get the user requests
     * @param query : a MyRequestsQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery
     * @see com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsList
     * @return a MyRequestsList object
     */
    public MyRequestsList getMyRequests(MyRequestsQuery query) {
        LOG.debug("[getMyRequests](query: {})", query);
        try {
            List<Case> caseList = List.of();
            long size = 0;
            if (searchService != null) {
                String searchQuery = myRequestsListQueryComposer.compose(query);
                LOG.debug("[getMyRequests] elasticsearchQuery:\n{}", searchQuery);
                long start = System.currentTimeMillis();
                JsonNode resultNode = searchService.query("case-instances", searchQuery);
                long end = System.currentTimeMillis();
                if (elasticsearchProperties.isLogResponseTimes()) {
                    LOG.info("[ELASTIC][getMyRequests] Successful elasticsearch request took {} ms", end - start);
                }
                ElasticResponse<Case> elasticResponse = objectMapper.readValue(objectMapper.treeAsTokens(resultNode), CASE_ELASTIC_RESPONSE_TYPE_REFERENCE);
                caseList = elasticResponse.getHits().getHitList().stream().map(Hit::getSource).collect(Collectors.toList());
                size = elasticResponse.getSize();
            }
            MyRequestsList result = new MyRequestsList(
                    caseList.stream().map(pagoNxtRequestItemSerializer::serialize).collect(Collectors.toList()),
                    size);
            LOG.debug("[getMyRequests] result: {}", result);
            return result;
        } catch (Exception e) {
            throw new ServiceException("Error listing my requests", "getMyRequests", e);
        }
    }
}
