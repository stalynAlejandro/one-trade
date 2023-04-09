package com.pagonxt.onetradefinance.work.service;

import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.security.CaseSecurityService;
import com.pagonxt.onetradefinance.work.utils.TaskUtils;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
class TaskOperationServiceTest {

    @InjectMocks
    private TaskOperationService taskOperationService;
    @Mock
    TaskService taskService;
    @Mock
    TaskUtils taskUtils;
    @Mock
    CaseSecurityService caseSecurityService;

    @Test
    void cancelCompleteInfoTask_ok_invokeTaskUtils() {
        // Given
        Map<String, Object> testVars = new HashMap<>();
        when(taskUtils.getTaskVariablesCompleteInfo("123")).thenReturn(testVars);

        AuthenticatedRequest request = mock(AuthenticatedRequest.class);
        UserInfo userInfo = mock(UserInfo.class);
        doReturn(userInfo).when(request).getRequester();
        User user = mock(User.class);
        doReturn(user).when(userInfo).getUser();
        // When
        taskOperationService.cancelCompleteInfoTask("123", request);
        // Then
        verify(taskUtils).getTaskVariablesCompleteInfo("123");
        verify(taskService, atLeast(2)).setVariable(eq("123"), any(), any());
        verify(caseSecurityService).checkEditTask(userInfo, "123");
    }
}
