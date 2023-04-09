package com.pagonxt.onetradefinance.work.parser.bpmn.usertask;

import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.expression.common.ClaimDisallowedExpression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.mockito.Mockito.*;

@UnitTest
class ClaimDisallowedExpressionTest {

    @InjectMocks
    ClaimDisallowedExpression claimDisallowedExpression;

    @Test
    void disableClaimForUser_sameAssignee_setsAssigneeNull() {
        // given
        DelegateExecution execution = mock(DelegateExecution.class);
        Task task = mock(Task.class);
        String userId = "some.user";
        doReturn(userId).when(task).getAssignee();
        String previousTaskCompletionUserId = userId;

        // when
        claimDisallowedExpression.disableClaimForUser(execution, task, previousTaskCompletionUserId);

        //  then
        verify(task, times(1)).setAssignee(null);
    }

    @Test
    void disableClaimForUser_differentAssignee_noAssigneeChanged() {
        // given
        DelegateExecution execution = mock(DelegateExecution.class);
        Task task = mock(Task.class);
        String userId = "some.user";
        doReturn(userId).when(task).getAssignee();
        String previousTaskCompletionUserId = "other.user";

        // when
        claimDisallowedExpression.disableClaimForUser(execution, task, previousTaskCompletionUserId);

        //  then
        verify(task, times(0)).setAssignee(null);
    }
}