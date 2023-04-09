package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.external.backend.api.model.ListRequest;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksList;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksQuery;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
class HistoricTasksServiceTest {

    @Mock
    private RestTemplateWorkService restTemplateWorkService;

    @InjectMocks
    HistoricTasksService historicTasksService;

    @Test
    void getHistoricTasks_whenPassingRequestList_thenCallWorkService() {
        // Given
        HistoricTasksList historicTasksList = new HistoricTasksList();
        when(restTemplateWorkService.postHistoricTasks(any())).thenReturn(historicTasksList);
        ListRequest listRequest = new ListRequest();
        listRequest.setFromPage(0);
        listRequest.setPageSize(5);
        listRequest.setSortField("startDate");
        listRequest.setSortOrder(-1);
        String code = "CLE-TEST";
        String locale = "es_es";
        UserInfo userInfo = new UserInfo();
        // When
        HistoricTasksList result = historicTasksService.getHistoricTasks(listRequest, code, locale, userInfo);
        // Then
        ArgumentCaptor<HistoricTasksQuery> captor = ArgumentCaptor.forClass(HistoricTasksQuery.class);
        verify(restTemplateWorkService, times(1)).postHistoricTasks(captor.capture());
        assertEquals(historicTasksList, result);
        HistoricTasksQuery historicTasksQuery = captor.getValue();
        assertEquals(code, historicTasksQuery.getCode());
        assertEquals(locale, historicTasksQuery.getLocale());
        assertEquals(userInfo, historicTasksQuery.getUserInfo());
        assertEquals(0, historicTasksQuery.getFromPage());
        assertEquals(5, historicTasksQuery.getPageSize());
        assertEquals("startDate", historicTasksQuery.getSortField());
        assertEquals(-1, historicTasksQuery.getSortOrder());
    }
}
