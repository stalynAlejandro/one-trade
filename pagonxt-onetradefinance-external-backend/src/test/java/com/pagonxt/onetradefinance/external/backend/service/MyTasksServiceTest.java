package com.pagonxt.onetradefinance.external.backend.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.*;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksList;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class MyTasksServiceTest {

    @Mock
    private RestTemplateWorkService restTemplateWorkService;

    @Spy
    @SuppressWarnings("unused") // Used by myTasksService
    ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @InjectMocks
    MyTasksService myTasksService;

    @Test
    void getMyTasks_whenPassingRequestList_thenCallWorkService() {
        // Given
        MyTasksList myTasksList = new MyTasksList();
        when(restTemplateWorkService.postMyTasks(any())).thenReturn(myTasksList);
        ListRequest listRequest = new ListRequest();
        Filters filters = new Filters();
        Filter filter = new Filter();
        filter.setValue("codeTest");
        filters.put("code", filter);
        listRequest.setFilters(filters);
        User requester = new User();
        // When
        MyTasksList result = myTasksService.getMyTasks(listRequest, "mine", requester);
        // Then
        ArgumentCaptor<MyTasksQuery> captor = ArgumentCaptor.forClass(MyTasksQuery.class);
        verify(restTemplateWorkService, times(1)).postMyTasks(captor.capture());
        assertEquals(myTasksList, result);
        MyTasksQuery myTasksQuery = captor.getValue();
        assertEquals(requester, myTasksQuery.getRequester());
        assertEquals("codeTest", myTasksQuery.getCode());
    }

    @Test
    void getFilters_ok_returnsFilters() {
        // Given
        String userType = "OFFICE";

        FiltersDefinition expectedResult = new FiltersDefinition();
        expectedResult.put("code", new FilterDefinition("text"));
        expectedResult.put("customerName", new FilterDefinition("text"));
        expectedResult.put("customerPersonNumber", new FilterDefinition("text"));
        expectedResult.put("customerTaxId", new FilterDefinition("text"));
        expectedResult.put("requesterName", new FilterDefinition("text"));
        expectedResult.put("ownerName", new FilterDefinition("text"));
        expectedResult.put("productId", new FilterDefinition("select", List.of(
                new FilterDefinitionItem("CLE", "Export Collection"),
                new FilterDefinitionItem("CLI", "Import Collection")
        )));
        expectedResult.put("eventId", new FilterDefinition("select", List.of(
                new FilterDefinitionItem("REQUEST", "Request"),
                new FilterDefinitionItem("MODIFICATION", "Modification")
        )));
        expectedResult.put("taskId", new FilterDefinition("select", List.of(
                new FilterDefinitionItem("EXTERNAL_REQUEST_DRAFT", "External Request Draft"),
                new FilterDefinitionItem("REVIEW", "Review"),
                new FilterDefinitionItem("REFERED_REVIEW", "Referred Review"),
                new FilterDefinitionItem("MANAGE", "Manage"),
                new FilterDefinitionItem("RELEASE", "Release"),
                new FilterDefinitionItem("REFERED_RELEASE", "Referred Release"),
                new FilterDefinitionItem("REFINE", "Refine"),
                new FilterDefinitionItem("SANCTION_AML_1A", "Sanction AML 1A"),
                new FilterDefinitionItem("SANCTION_AML_1B", "Sanction AML 1B"),
                new FilterDefinitionItem("SANCTION_AML_2", "Sanction AML 2"),
                new FilterDefinitionItem("REVIEW_CANCELATION", "Review Cancellation")
        )));
        expectedResult.put("priority", new FilterDefinition("select", List.of(
                new FilterDefinitionItem("normal", "Normal"),
                new FilterDefinitionItem("urgent", "Urgent")
        )));
        expectedResult.put("currency", new FilterDefinition("select", List.of(
                new FilterDefinitionItem("EUR", "currency_EUR"),
                new FilterDefinitionItem("GBP", "currency_GBP")
        )));
        expectedResult.put("fromDate", new FilterDefinition("date"));
        expectedResult.put("toDate", new FilterDefinition("date"));
        expectedResult.put("fromAmount", new FilterDefinition("number"));
        expectedResult.put("toAmount", new FilterDefinition("number"));

        // When
        FiltersDefinition result = myTasksService.getFilters(userType);

        // Then
        assertEquals(expectedResult, result, "Result should match pattern");
    }
}
