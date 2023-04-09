package com.pagonxt.onetradefinance.work.parser.bpmn.usertask;

import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.expression.common.SetCompletionUserExpression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.mockito.Mockito.*;

@UnitTest
class SetCompletionUserExpressionTest {

    @InjectMocks
    SetCompletionUserExpression setCompletionUserExpression;

    @Test
    void addCompletionUser() {
        // given
        DelegateExecution execution = mock(DelegateExecution.class);
        Task task = mock(Task.class);
        String userId = "some.user";
        doReturn(userId).when(task).getAssignee();
        String completionUserVariable = "completionUser";

        // when
        setCompletionUserExpression.addCompletionUser(execution, task, completionUserVariable);

        // then
        verify(execution, times(1)).setVariable(completionUserVariable, userId);
    }
}