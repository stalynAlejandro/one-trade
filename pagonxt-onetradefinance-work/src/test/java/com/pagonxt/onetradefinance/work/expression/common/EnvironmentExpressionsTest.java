package com.pagonxt.onetradefinance.work.expression.common;

import com.flowable.platform.service.task.PlatformTaskService;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class EnvironmentExpressionsTest {

    PlatformTaskService platformTaskService = mock(PlatformTaskService.class);

    EnvironmentExpressions environmentExpressions = new EnvironmentExpressions(platformTaskService, "testbaseurl");

    @Test
    void getExternalTaskUrl_ok_returnsCorrectUrl() {
        // Given
        Map<String, Object> variables = new HashMap<>();
        Map<String, Object> root = new HashMap<>();
        root.put("product", "PRODUCT_TEST");
        root.put("event", "EVENT_TEST");
        variables.put("root", root);
        variables.put("externalTaskType", "TASKTYPE_TEST");

        when(platformTaskService.getTaskVariables("TSK-123")).thenReturn(variables);
        // When
        String result = environmentExpressions.getExternalTaskUrl("TSK-123");
        // Then
        assertEquals("testbaseurl/app/product-test/event-test/tasktype-test/TSK-123", result);
    }

    @Test
    void getExternalTaskUrl_whenTaskVariablesNotFound_returnsNull() {
        // Given
        when(platformTaskService.getTaskVariables("TSK-123")).thenReturn(null);
        // When
        String result = environmentExpressions.getExternalTaskUrl("TSK-123");
        // Then
        assertNull(result);
    }

    @Test
    void getExternalTaskUrl_whenRootNotFound_returnsNull() {
        // Given
        Map<String, Object> variables = new HashMap<>();
        variables.put("root", null);
        variables.put("externalTaskType", "TASKTYPE_TEST");
        when(platformTaskService.getTaskVariables("TSK-123")).thenReturn(variables);
        // When
        String result = environmentExpressions.getExternalTaskUrl("TSK-123");
        // Then
        assertNull(result);
    }

    @Test
    void getExternalRequestsQueryUrl_ok_returnUrl(){
        // Given
        // When
        String result = environmentExpressions.getExternalRequestsQueryUrl();

        // Then
        assertEquals("testbaseurl/app/requests-query", result);
    }
}
