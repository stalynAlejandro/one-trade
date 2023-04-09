package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.external.backend.api.model.Filter;
import com.pagonxt.onetradefinance.external.backend.api.model.Filters;
import com.pagonxt.onetradefinance.external.backend.api.model.ListRequest;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsList;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery;
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
class MyRequestsServiceTest {

    @InjectMocks
    MyRequestsService myRequestsService;

    @Mock
    private RestTemplateWorkService restTemplateWorkService;

    @Test
    void getMyTasks_whenPassingRequestList_thenCallWorkService() {
        // Given
        MyRequestsList myRequestsList = new MyRequestsList();
        when(restTemplateWorkService.postMyRequests(any())).thenReturn(myRequestsList);
        ListRequest listRequest = new ListRequest();
        Filters filters = new Filters();
        Filter filter = new Filter();
        filter.setValue("123.45");
        filters.put("fromAmount", filter);
        listRequest.setFilters(filters);
        User requester = new User();
        // When
        MyRequestsList result = myRequestsService.getMyRequests(listRequest, requester);
        // Then
        ArgumentCaptor<MyRequestsQuery> captor = ArgumentCaptor.forClass(MyRequestsQuery.class);
        verify(restTemplateWorkService, times(1)).postMyRequests(captor.capture());
        assertEquals(myRequestsList, result);
        MyRequestsQuery myRequestsQuery = captor.getValue();
        assertEquals(requester, myRequestsQuery.getRequester());
        assertEquals(123.45d, myRequestsQuery.getFromAmount());
    }

}
