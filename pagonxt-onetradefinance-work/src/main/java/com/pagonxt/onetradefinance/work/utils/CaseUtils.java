package com.pagonxt.onetradefinance.work.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.indexing.SearchService;
import com.flowable.platform.service.work.PlatformEntityLinkService;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.work.configuration.ElasticsearchProperties;
import com.pagonxt.onetradefinance.work.serializer.CaseQueryComposer;
import com.pagonxt.onetradefinance.work.service.model.Case;
import com.pagonxt.onetradefinance.work.service.model.elastic.ElasticResponse;
import com.pagonxt.onetradefinance.work.service.model.elastic.Hit;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.scope.ScopeTypes;
import org.flowable.content.api.ContentItem;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.entitylink.api.EntityLink;
import org.flowable.entitylink.api.HierarchyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Class with case utils
 * @author -
 * @version jdk-11.0.13
 * @see org.flowable.engine.RuntimeService
 * @see com.flowable.indexing.SearchService
 * @see com.pagonxt.onetradefinance.work.serializer.CaseQueryComposer
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see com.flowable.platform.service.work.PlatformEntityLinkService
 * @since jdk-11.0.13
 */
@Component
public class CaseUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CaseUtils.class);

    private static final TypeReference<ElasticResponse<Case>> CASE_ELASTIC_RESPONSE_TYPE_REFERENCE =
            new TypeReference<>() {
            };

    //class attributes
    private final ElasticsearchProperties elasticsearchProperties;
    private final RuntimeService runtimeService;
    private final SearchService searchService;
    private final CaseQueryComposer caseQueryComposer;
    private final ObjectMapper objectMapper;
    private final PlatformEntityLinkService platformEntityLinkService;

    /**
     * constructor method
     *
     * @param elasticsearchProperties   : an ElasticsearchProperties object
     * @param runtimeService            : a RuntimeService object
     * @param searchService             : a SearchService object
     * @param caseQueryComposer         : a CaseQueryComposer object
     * @param objectMapper              : an ObjectMapper object
     * @param platformEntityLinkService : a PlaPlatformEntityLinkService object
     */
    public CaseUtils(ElasticsearchProperties elasticsearchProperties,
                     RuntimeService runtimeService,
                     @Nullable SearchService searchService,
                     CaseQueryComposer caseQueryComposer,
                     ObjectMapper objectMapper,
                     PlatformEntityLinkService platformEntityLinkService) {
        this.elasticsearchProperties = elasticsearchProperties;
        this.runtimeService = runtimeService;
        this.searchService = searchService;
        this.caseQueryComposer = caseQueryComposer;
        this.objectMapper = objectMapper;
        this.platformEntityLinkService = platformEntityLinkService;
    }

    /**
     * Method to get case id from descendant execution
     * @param execution : a DelegateExecution object
     * @see org.flowable.engine.delegate.DelegateExecution
     * @return a string object with the case id
     */
    public String getCaseIdFromDescendantExecution(DelegateExecution execution) {
        String caseInstanceId;

        List<EntityLink> entityLinkParentsForProcessInstance =
                runtimeService.getEntityLinkParentsForProcessInstance(execution.getProcessInstanceId());

        Predicate<EntityLink> isRootCmmnCase = entityLink -> entityLink.getScopeType().equals(ScopeTypes.CMMN)
                && entityLink.getHierarchyType().equals(HierarchyType.ROOT);

        EntityLink first = entityLinkParentsForProcessInstance.stream()
                .filter(isRootCmmnCase)
                .findFirst()
                .orElseThrow(() -> new FlowableException("Unable to locate root case instance"));

        caseInstanceId = first.getScopeId();

        return caseInstanceId;
    }

    /**
     * Method to get a case by code
     * @param code  : the string object with the code
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     * @return a Case object
     */
    public Case getCaseByCode(String code) {
        if (searchService == null) {
            return null;
        }
        try {
            long start = System.currentTimeMillis();
            JsonNode resultNode =
                    searchService.query("case-instances", caseQueryComposer.composeGetCaseByCodeQuery(code));
            long end = System.currentTimeMillis();
            if (elasticsearchProperties.isLogResponseTimes()) {
                LOG.info("[ELASTIC][getCaseByCode] Successful elasticsearch request took {} ms", end - start);
            }
            return processResultNode(resultNode);
        } catch (Exception e) {
            throw new ServiceException("Error fetching case", "getCase", e);
        }
    }

    /**
     * Method to get a case by id
     * @param id  : the string object with the id
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     * @return a Case object
     */
    public Case getCaseById(String id) {
        if (searchService == null) {
            return null;
        }
        try {
            long start = System.currentTimeMillis();
            JsonNode resultNode = searchService.query("case-instances",
                    caseQueryComposer.composeGetCaseByIdQuery(id));
            long end = System.currentTimeMillis();
            if (elasticsearchProperties.isLogResponseTimes()) {
                LOG.info("[ELASTIC][getCaseById] Successful elasticsearch request took {} ms", end - start);
            }
            return processResultNode(resultNode);
        } catch (Exception e) {
            throw new ServiceException("Error fetching case", "getCase", e);
        }
    }

    /**
     * Method to get the document case id
     * @param contentItem : a ContentItem object
     * @see org.flowable.content.api.ContentItem
     * @return a string with the case id
     */
    public String getDocumentCaseId(ContentItem contentItem) {
        if (ScopeTypes.CMMN.equals(contentItem.getScopeType())) {
            return contentItem.getScopeId();
        }
        if (contentItem.getProcessInstanceId() != null) {
            // Get an identityLink referencing the root element,
            // from a process id. The scopeId of that identityLink is our case id.
            return platformEntityLinkService.getHistoricParentEntityLinkWithName(ScopeTypes.BPMN,
                    contentItem.getProcessInstanceId(), HierarchyType.ROOT, false).getScopeId();
        }
        if (contentItem.getTaskId() != null) {
            // Get an identityLink referencing the root element,
            // from a task id. The scopeId of that identityLink is our case id.
            return platformEntityLinkService.getHistoricParentEntityLinkWithName(ScopeTypes.TASK,
                    contentItem.getTaskId(), HierarchyType.ROOT, false).getScopeId();
        }
        return null;
    }

    /**
     * Method to process result node
     * @param node : a JsonNode object
     * @return a Case object
     * @see com.fasterxml.jackson.databind.JsonNode
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     * @throws IOException for low-level read issues, or JsonParseException for decoding problems
     */
    private Case processResultNode(JsonNode node) throws IOException {
        ElasticResponse<Case> elasticResponse = objectMapper.readValue(objectMapper.treeAsTokens(node),
                CASE_ELASTIC_RESPONSE_TYPE_REFERENCE);
        List<Case> caseList = elasticResponse.getHits().getHitList().parallelStream()
                .map(Hit::getSource).collect(Collectors.toList());
        return caseList.stream().findFirst().orElse(null);
    }
}
