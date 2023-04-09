package com.pagonxt.onetradefinance.work.expression.common;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * class for set a completion user expression
 * @author -
 * @version jdk-11.0.13
 * @see org.flowable.engine.delegate.DelegateExecution
 * @see org.flowable.task.api.Task
 * @since jdk-11.0.13
 */
@Component
public class SetCompletionUserExpression {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(SetCompletionUserExpression.class);

    /**
     * Method to add the completion user
     * @param execution                     : a DelegateExecution object
     * @param task                          : a Task object
     * @param completionUserVariable        : a string with completion user
     */
    public void addCompletionUser(DelegateExecution execution,
                                  Task task,
                                  String completionUserVariable) {
        LOG.debug("addCompletionUser: taskId={}, task.assignee={}, completionUserVariable={}",
                task.getId(), task.getAssignee(),
                completionUserVariable);

        execution.setVariable(completionUserVariable, task.getAssignee());
    }
}
