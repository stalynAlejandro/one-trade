package com.pagonxt.onetradefinance.work.utils;

import com.aspose.slides.Collections.ArrayList;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flowable.indexing.SearchService;
import com.flowable.platform.service.work.EntityLinkWithName;
import com.flowable.platform.service.work.PlatformEntityLinkService;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.configuration.ElasticsearchProperties;
import com.pagonxt.onetradefinance.work.serializer.CaseQueryComposer;
import com.pagonxt.onetradefinance.work.service.model.Case;
import com.pagonxt.onetradefinance.work.service.model.elastic.ElasticResponse;
import com.pagonxt.onetradefinance.work.service.model.elastic.Hit;
import com.pagonxt.onetradefinance.work.service.model.elastic.Hits;
import com.pagonxt.onetradefinance.work.service.model.elastic.Total;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.scope.ScopeTypes;
import org.flowable.content.api.ContentItem;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.entitylink.api.EntityLink;
import org.flowable.entitylink.api.HierarchyType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class CaseUtilsTest {

    @Spy
    private ElasticsearchProperties elasticsearchProperties = new ElasticsearchProperties();

    @Mock
    private RuntimeService runtimeService;

    @Mock
    private SearchService searchService;

    @Mock
    private CaseQueryComposer caseQueryComposer;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    PlatformEntityLinkService platformEntityLinkService;

    @Spy
    private ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.USE_LONG_FOR_INTS, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    @InjectMocks
    CaseUtils caseUtils;

    @Test
    void getCaseIdFromDescendantExecution_caseExists_returnsCaseId() {
        // Given
        DelegateExecution delegateExecution = mock(DelegateExecution.class);
        @SuppressWarnings("unchecked")
        EntityLink entityLink = mock(EntityLink.class);
        when(entityLink.getScopeType()).thenReturn(ScopeTypes.CMMN);
        when(entityLink.getHierarchyType()).thenReturn(HierarchyType.ROOT);
        List<EntityLink> entityLinkParentsForProcessInstance = List.of(entityLink);
        String processInstanceId = "processInstanceId";
        doReturn(processInstanceId).when(delegateExecution).getProcessInstanceId();
        doReturn(entityLinkParentsForProcessInstance).when(runtimeService).getEntityLinkParentsForProcessInstance(processInstanceId);

        @SuppressWarnings("unchecked")
        Stream<EntityLink> stream = mock(Stream.class);
        Optional<EntityLink> optional = Optional.of(entityLink);
        String caseInstanceId = "caseInstanceId";
        doReturn(caseInstanceId).when(entityLink).getScopeId();

        // When
        String result = caseUtils.getCaseIdFromDescendantExecution(delegateExecution);

        // Then
        String expectedMessage = "caseInstanceId";
        assertThat(result).isEqualTo(expectedMessage);
    }

    @Test
    void getCaseIdFromDescendantExecution_caseInstanceNotExists_returnsException() {
        // Given
        DelegateExecution delegateExecution = mock(DelegateExecution.class);
        @SuppressWarnings("unchecked")
        List<EntityLink> entityLinkParentsForProcessInstance = mock(ArrayList.class);
        String processInstanceId = "processInstanceId";
        doReturn(processInstanceId).when(delegateExecution).getProcessInstanceId();
        doReturn(entityLinkParentsForProcessInstance).when(runtimeService).getEntityLinkParentsForProcessInstance(processInstanceId);

        // When
        Exception exception = assertThrows(FlowableException.class, () ->
                caseUtils.getCaseIdFromDescendantExecution(delegateExecution));

        // Then
        String expectedMessage = "Unable to locate root case instance";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void getCaseIdFromDescendantExecution_wrongScope_throwsException() {
        // Given
        DelegateExecution delegateExecution = mock(DelegateExecution.class);
        @SuppressWarnings("unchecked")
        List<EntityLink> entityLinkParentsForProcessInstance = mock(ArrayList.class);
        String processInstanceId = "processInstanceId";
        doReturn(processInstanceId).when(delegateExecution).getProcessInstanceId();
        doReturn(entityLinkParentsForProcessInstance).when(runtimeService).getEntityLinkParentsForProcessInstance(processInstanceId);

        EntityLink entityLink = mock(EntityLink.class);
        when(entityLink.getScopeType()).thenReturn(ScopeTypes.CMMN);
        when(entityLink.getHierarchyType()).thenReturn(HierarchyType.PARENT);
        when(runtimeService.getEntityLinkParentsForProcessInstance("processInstanceId")).thenReturn(List.of(entityLink));

        // When
        Exception exception = Assertions.assertThrows(FlowableException.class, () -> caseUtils.getCaseIdFromDescendantExecution(delegateExecution), "Should throw FlowableException");

        // Then
        assertEquals("Unable to locate root case instance", exception.getMessage(), "Exception thrown should contain a valid exception message");
    }

    @Test
    void getCaseByCode_error_throwsServiceException() {
        // Given
        String code = "CLE-1";
        doReturn("query").when(caseQueryComposer).composeGetCaseByCodeQuery(code);
        doThrow(new NullPointerException("error")).when(searchService).query("case-instances", "query");

        // When
        Exception thrown = assertThrows(ServiceException.class,
                () -> caseUtils.getCaseByCode(code),
                "Should throw ServiceException");

        // Then
        assertEquals("Error fetching case", thrown.getMessage(), "Exception should have a valid message");
    }

    @Test
    void getCaseByCode_nullSearchService_returnsNull() {
        // Given
        CaseUtils nullSearchServiceCaseUtils = new CaseUtils(new ElasticsearchProperties(), runtimeService,
                null, caseQueryComposer, mapper, platformEntityLinkService);

        // When and then
        assertNull(nullSearchServiceCaseUtils.getCaseByCode("code"), "Should return null");
    }

    @Test
    void getCaseByCode_ok_returnsCase() {
        // Given
        String code = "CLE-1";
        Case caseItem = new Case();
        caseItem.setId(code);
        doReturn("query").when(caseQueryComposer).composeGetCaseByCodeQuery(code);
        ElasticResponse<Case> elasticResponse = new ElasticResponse<>();
        Hits<Case> hits = new Hits<>();
        hits.setTotal(new Total());
        hits.getTotal().setValue(1);
        elasticResponse.setHits(hits);
        Hit<Case> hit = new Hit<>();
        hit.setSource(caseItem);
        elasticResponse.getHits().setHitList(List.of(hit));
        JsonNode searchServiceResultNode = mapper.valueToTree(elasticResponse);
        doReturn(searchServiceResultNode).when(searchService).query("case-instances", "query");
        elasticsearchProperties.setLogResponseTimes(true);

        // When
        Case result = caseUtils.getCaseByCode(code);

        // Then
        assertEquals(caseItem, result, "Result should match pattern");
        elasticsearchProperties.setLogResponseTimes(false);
    }

    @Test
    void getCaseByCode_null_returnsNull() {
        // Given
        doReturn("query").when(caseQueryComposer).composeGetCaseByCodeQuery(null);
        ElasticResponse<Case> elasticResponse = new ElasticResponse<>();
        Hits<Case> hits = new Hits<>();
        hits.setTotal(new Total());
        hits.getTotal().setValue(0);
        elasticResponse.setHits(hits);
        elasticResponse.getHits().setHitList(List.of());
        JsonNode searchServiceResultNode = mapper.valueToTree(elasticResponse);
        doReturn(searchServiceResultNode).when(searchService).query("case-instances", "query");

        // When
        Case result = caseUtils.getCaseByCode(null);

        // Then
        assertNull(result, "Should return null");
    }

    @Test
    void getCaseByCode_notFound_returnsNull() {
        // Given
        String code = "CLE-1";
        doReturn("query").when(caseQueryComposer).composeGetCaseByCodeQuery(code);
        ElasticResponse<Case> elasticResponse = new ElasticResponse<>();
        Hits<Case> hits = new Hits<>();
        hits.setTotal(new Total());
        hits.getTotal().setValue(0);
        elasticResponse.setHits(hits);
        elasticResponse.getHits().setHitList(List.of());
        JsonNode searchServiceResultNode = mapper.valueToTree(elasticResponse);
        doReturn(searchServiceResultNode).when(searchService).query("case-instances", "query");

        // When
        Case result = caseUtils.getCaseByCode(code);

        // Then
        assertNull(result, "Should return null");
    }

    @Test
    void getCaseById_error_throwsServiceException() {
        // Given
        String id = "caseId";
        doReturn("query").when(caseQueryComposer).composeGetCaseByIdQuery(id);
        doThrow(new NullPointerException("error")).when(searchService).query("case-instances", "query");

        // When
        Exception thrown = assertThrows(ServiceException.class,
                () -> caseUtils.getCaseById(id),
                "Should throw ServiceException");

        // Then
        assertEquals("Error fetching case", thrown.getMessage(), "Exception should have a valid message");
    }

    @Test
    void getCaseById_nullSearchService_returnsNull() {
        // Given
        CaseUtils nullSearchServiceCaseUtils = new CaseUtils(new ElasticsearchProperties(), runtimeService,
                null, caseQueryComposer, mapper, platformEntityLinkService);

        // When and then
        assertNull(nullSearchServiceCaseUtils.getCaseById("id"), "Should return null");
    }

    @Test
    void getCaseById_ok_returnsCase() {
        // Given
        String id = "caseId";
        Case caseItem = new Case();
        caseItem.setId(id);
        doReturn("query").when(caseQueryComposer).composeGetCaseByIdQuery(id);
        ElasticResponse<Case> elasticResponse = new ElasticResponse<>();
        Hits<Case> hits = new Hits<>();
        hits.setTotal(new Total());
        hits.getTotal().setValue(1);
        elasticResponse.setHits(hits);
        Hit<Case> hit = new Hit<>();
        hit.setSource(caseItem);
        elasticResponse.getHits().setHitList(List.of(hit));
        JsonNode searchServiceResultNode = mapper.valueToTree(elasticResponse);
        doReturn(searchServiceResultNode).when(searchService).query("case-instances", "query");
        elasticsearchProperties.setLogResponseTimes(true);

        // When
        Case result = caseUtils.getCaseById(id);

        // Then
        assertEquals(caseItem, result, "Result should match pattern");
        elasticsearchProperties.setLogResponseTimes(false);
    }

    @Test
    void getCaseById_null_returnsNull() {
        // Given
        doReturn("query").when(caseQueryComposer).composeGetCaseByIdQuery(any());
        ElasticResponse<Case> elasticResponse = new ElasticResponse<>();
        Hits<Case> hits = new Hits<>();
        hits.setTotal(new Total());
        hits.getTotal().setValue(0);
        elasticResponse.setHits(hits);
        elasticResponse.getHits().setHitList(List.of());
        JsonNode searchServiceResultNode = mapper.valueToTree(elasticResponse);
        doReturn(searchServiceResultNode).when(searchService).query("case-instances", "query");

        // When
        Case result = caseUtils.getCaseById(null);

        // Then
        assertNull(result, "Should return null");
    }

    @Test
    void getCaseById_notFound_returnsNull() {
        // Given
        String id = "caseId";
        doReturn("query").when(caseQueryComposer).composeGetCaseByIdQuery(id);
        ElasticResponse<Case> elasticResponse = new ElasticResponse<>();
        Hits<Case> hits = new Hits<>();
        hits.setTotal(new Total());
        hits.getTotal().setValue(0);
        elasticResponse.setHits(hits);
        elasticResponse.getHits().setHitList(List.of());
        JsonNode searchServiceResultNode = mapper.valueToTree(elasticResponse);
        doReturn(searchServiceResultNode).when(searchService).query("case-instances", "query");

        // When
        Case result = caseUtils.getCaseById(id);

        // Then
        assertNull(result, "Should return null");
    }

    @Test
    void getDocumentCaseId_documentHasCmmnScopeId_returnsCaseId() {
        // Given
        ContentItem document = mock(ContentItem.class);
        when(document.getScopeType()).thenReturn(ScopeTypes.CMMN);
        when(document.getScopeId()).thenReturn("caseId");

        // When
        String result = caseUtils.getDocumentCaseId(document);

        // Then
        assertEquals("caseId", result, "Result should match patten");
    }

    @Test
    void getDocumentCaseId_documentHasProcessInstanceId_invokesPlatformEntityLinkService() {
        // Given
        ContentItem document = mock(ContentItem.class);
        when(document.getScopeType()).thenReturn(null);
        when(document.getProcessInstanceId()).thenReturn("processInstanceId");
        EntityLinkWithName parentEntityLink = mock(EntityLinkWithName.class);
        when(parentEntityLink.getScopeId()).thenReturn("caseId");
        when(platformEntityLinkService.getHistoricParentEntityLinkWithName(ScopeTypes.BPMN,
                document.getProcessInstanceId(), HierarchyType.ROOT, false)).thenReturn(parentEntityLink);

        // When
        String result = caseUtils.getDocumentCaseId(document);

        // Then
        verify(platformEntityLinkService).getHistoricParentEntityLinkWithName(ScopeTypes.BPMN,
                document.getProcessInstanceId(), HierarchyType.ROOT, false);
        assertEquals("caseId", result, "Result should match patten");
    }

    @Test
    void getDocumentCaseId_documentHasPTaskId_invokesPlatformEntityLinkService() {
        // Given
        ContentItem document = mock(ContentItem.class);
        when(document.getScopeType()).thenReturn(null);
        when(document.getProcessInstanceId()).thenReturn(null);
        when(document.getTaskId()).thenReturn("taskId");
        EntityLinkWithName parentEntityLink = mock(EntityLinkWithName.class);
        when(parentEntityLink.getScopeId()).thenReturn("caseId");
        when(platformEntityLinkService.getHistoricParentEntityLinkWithName(ScopeTypes.TASK,
                document.getTaskId(), HierarchyType.ROOT, false)).thenReturn(parentEntityLink);

        // When
        String result = caseUtils.getDocumentCaseId(document);

        // Then
        verify(platformEntityLinkService).getHistoricParentEntityLinkWithName(ScopeTypes.TASK,
                document.getTaskId(), HierarchyType.ROOT, false);
        assertEquals("caseId", result, "Result should match patten");
    }

    @Test
    void getDocumentCaseId_noValidInfo_returnsNull() {
        // Given
        ContentItem document = mock(ContentItem.class);
        when(document.getScopeType()).thenReturn(null);
        when(document.getProcessInstanceId()).thenReturn(null);
        when(document.getTaskId()).thenReturn(null);

        // When
        String result = caseUtils.getDocumentCaseId(document);

        assertNull(result, "Result should be null");
    }
}
