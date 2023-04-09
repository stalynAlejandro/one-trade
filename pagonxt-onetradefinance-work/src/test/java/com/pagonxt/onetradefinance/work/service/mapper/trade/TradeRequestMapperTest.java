package com.pagonxt.onetradefinance.work.service.mapper.trade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flowable.content.engine.impl.persistence.entity.ContentItemEntity;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.work.common.CaseCommonVariablesHelper;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.mapper.OperationDocumentMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@UnitTest
class TradeRequestMapperTest {
    @InjectMocks
    TradeRequestMapper tradeRequestMapper;
    @Mock
    private OperationDocumentMapper operationDocumentMapper;
    @Mock
    private CaseCommonVariablesHelper caseCommonVariablesHelper;
    @Test
    void mapToTradeRequest_whenNoDraft_returnValidObject() throws JsonProcessingException {
        Map<String, Object> caseInstanceVariables = new HashMap<>();
        String userType = "OFFICE";
        DataObjectInstanceVariableContainerImpl operation = mock(DataObjectInstanceVariableContainerImpl.class);
        doReturn("requesterId1").when(operation).getString("requesterId");
        doReturn("requesterDisplayedName1").when(operation).getString("requesterDisplayedName");
        doReturn("requesterType1").when(operation).getString("requesterType");
        doReturn("country1").when(operation).getString("country");
        doReturn("office1").when(operation).getString("office");
        doReturn("middleOffice1").when(operation).getString("middleOffice");
        doReturn("priority1").when(operation).getString("priority");
        doReturn("comment1").when(operation).getString("comment");
        doReturn("customerId1").when(operation).getString("customerID");
        doReturn("customerFullName1").when(operation).getString("customerFullName");
        doReturn("customerCode1").when(operation).getString("customerCode");
        doReturn("customerSegment1").when(operation).getString("customerSegment");
        doReturn("customerEmail1").when(operation).getString("customerEmail");
        doReturn("NO_DRAFT").when(operation).getString("displayedStatus");
        caseInstanceVariables.put("operationCode", "CLI-001");
        caseInstanceVariables.put(PRODUCT, "CLI");
        caseInstanceVariables.put(EVENT, "Request");
        caseInstanceVariables.put("operation", operation);
        Date date1 = new Date();
        when(operation.getDate(CREATION_DATE)).thenReturn(date1);
        List<ContentItemEntity> operationDocuments = mock(List.class);
        caseInstanceVariables.put("operationDocuments", operationDocuments);
        List<Document> documents = Collections.emptyList();
        when(operationDocumentMapper.mapOperationDocuments(operationDocuments)).thenReturn(documents);
        TradeRequest request = tradeRequestMapper.mapToTradeRequest(caseInstanceVariables,userType);

        assertEquals("CLI-001", request.getCode());
        assertEquals("CLI", request.getProduct());
        assertEquals("Request", request.getEvent());
        verify(operationDocumentMapper).mapOperationDocuments(operationDocuments);

    }
    @Test
    void mapToTradeRequest_whenIsDraft_returnValidObject() throws JsonProcessingException {
        String userType = "OFFICE";
        DataObjectInstanceVariableContainerImpl operation = mock(DataObjectInstanceVariableContainerImpl.class);
        doReturn("requesterId1").when(operation).getString("requesterId");
        doReturn("requesterDisplayedName1").when(operation).getString("requesterDisplayedName");
        doReturn("requesterType1").when(operation).getString("requesterType");
        doReturn("country1").when(operation).getString("country");
        doReturn("office1").when(operation).getString("office");
        doReturn("middleOffice1").when(operation).getString("middleOffice");
        doReturn("priority1").when(operation).getString("priority");
        doReturn("comment1").when(operation).getString("comment");
        doReturn("customerId1").when(operation).getString("customerID");
        doReturn("customerFullName1").when(operation).getString("customerFullName");
        doReturn("customerCode1").when(operation).getString("customerCode");
        doReturn("customerSegment1").when(operation).getString("customerSegment");
        doReturn("customerEmail1").when(operation).getString("customerEmail");
        doReturn("DRAFT").when(operation).getString("displayedStatus");
        Map<String, Object> caseInstanceVariables = new HashMap<>();
        TradeRequest request;
        caseInstanceVariables.put("operation", operation);
        Object requestDocuments = mock(Object.class);
        caseInstanceVariables.put("requestDocuments", requestDocuments);

        caseInstanceVariables.put("operationCode", "CLI-001");
        caseInstanceVariables.put(PRODUCT, "CLI");
        caseInstanceVariables.put(EVENT, "Request");
        caseInstanceVariables.put("operation", operation);
        request = tradeRequestMapper.mapToTradeRequest(caseInstanceVariables,userType);
        verify(caseCommonVariablesHelper).convertToDocumentList(requestDocuments);
        assertEquals("CLI-001", request.getCode());
        assertEquals("CLI",request.getProduct());
        assertEquals("Request",request.getEvent());
    }

}
