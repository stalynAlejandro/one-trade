package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.Comment;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
class CaseDataServiceTest {

    @InjectMocks
    CaseDataService caseDataService;

    @Mock
    RestTemplateWorkService restTemplateWorkService;

    @Test
    void getComments_whenPassingTaskId_thenReturnComments() {
        // Given
        Comment comment = new Comment();
        comment.setTaskName("Case 123");
        List<Comment> comments = List.of(comment);
        ControllerResponse controllerResponse = ControllerResponse.success("", comments);
        when(restTemplateWorkService.findComments(eq("CLE-TEST"), any())).thenReturn(controllerResponse);
        UserInfo userInfo = new UserInfo();
        // When
        List<Comment> response = caseDataService.getComments("CLE-TEST", userInfo);
        // Then
        ArgumentCaptor<AuthenticatedRequest> captor = ArgumentCaptor.forClass(AuthenticatedRequest.class);
        verify(restTemplateWorkService).findComments(eq("CLE-TEST"), captor.capture());
        assertEquals(userInfo,  captor.getValue().getRequester());
        assertEquals("Case 123", response.get(0).getTaskName());
    }

}
