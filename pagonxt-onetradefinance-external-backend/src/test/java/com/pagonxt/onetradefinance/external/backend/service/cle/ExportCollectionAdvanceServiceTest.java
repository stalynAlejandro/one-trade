package com.pagonxt.onetradefinance.external.backend.service.cle;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvance;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionQuery;
import com.pagonxt.onetradefinance.integrations.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class ExportCollectionAdvanceServiceTest {

    @Mock
    private RestTemplateWorkService restTemplateWorkService;

    @InjectMocks
    private ExportCollectionAdvanceService exportCollectionAdvanceService;

    @Test
    void getCustomerExportCollectionAdvances_whenPassingCustomerAndUser_thenInvokePostExportCollections() {
        // Given
        String customerName = "BUC-1234567";
        User user = new User("userId", "userName", "userType");
        ExportCollectionAdvance exportCollectionAdvance = new ExportCollectionAdvance();
        exportCollectionAdvance.setCode("Code1");
        List<ExportCollectionAdvance> response = List.of(exportCollectionAdvance);
        ControllerResponse controllerResponse = ControllerResponse.success("", response);
        when(restTemplateWorkService.postExportCollections(any())).thenReturn(controllerResponse);
        // When
        List<ExportCollectionAdvance> result = exportCollectionAdvanceService.getCustomerExportCollectionAdvances(customerName, user);
        // Then
        ArgumentCaptor<ExportCollectionQuery> captor = ArgumentCaptor.forClass(ExportCollectionQuery.class);
        verify(restTemplateWorkService, times(1)).postExportCollections(captor.capture());
        ExportCollectionQuery query = captor.getValue();
        assertEquals(user, query.getRequester());
        assertEquals(customerName, query.getCustomerPersonNumber());
        assertEquals("advance", query.getCollectionType());
        assertEquals("Code1", result.get(0).getCode());
    }
}
