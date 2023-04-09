package com.pagonxt.onetradefinance.work.expression.common;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * class for disable claims
 * @author -
 * @version jdk-11.0.13
 * @see org.flowable.engine.delegate.DelegateExecution
 * @see org.flowable.task.api.Task
 * @since jdk-11.0.13
 */
@Component
@SuppressWarnings("unused")
public class ClaimDisallowedExpression {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(ClaimDisallowedExpression.class);

    /**
     * Method to disable claim for user
     * @param execution                     : a DelegateExecution object
     * @param task                          : a Task object
     * @param previousTaskCompletionUserId  : a string with user id that has completed the previous task
     */
    public void disableClaimForUser(DelegateExecution execution,
                                    Task task,
                                    String previousTaskCompletionUserId) {
        LOG.debug("disableClaimForUser: taskId={}, task.assignee={}, previousTaskCompletionUserId={}",
                task.getId(), task.getAssignee(), previousTaskCompletionUserId);

        if (Objects.equals(task.getAssignee(), previousTaskCompletionUserId)) {
            task.setAssignee(null);
        }
    }
}
