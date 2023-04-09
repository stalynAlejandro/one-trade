package com.pagonxt.onetradefinance.work.service;

import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.work.security.CaseSecurityService;
import com.pagonxt.onetradefinance.work.utils.TaskUtils;
import org.flowable.engine.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;

/**
 * service class for tasks operations
 * @author -
 * @version jdk-11.0.13
 * @see  org.flowable.engine.TaskService
 * @see com.pagonxt.onetradefinance.work.utils.TaskUtils
 * @see com.pagonxt.onetradefinance.work.security.CaseSecurityService
 * @since jdk-11.0.13
 */
@Service
public class TaskOperationService {

    //class attributes
    private final TaskService taskService;
    private final TaskUtils taskUtils;
    private final CaseSecurityService caseSecurityService;

    /**
     * constructor method
     * @param taskService           : a TaskService object
     * @param taskUtils             : a TaskUtils object
     * @param caseSecurityService   : a CaseSecurityService object
     */
    public TaskOperationService(TaskService taskService,
                                TaskUtils taskUtils,
                                CaseSecurityService caseSecurityService) {
        this.taskService = taskService;
        this.taskUtils = taskUtils;
        this.caseSecurityService = caseSecurityService;
    }

    /**
     * Method to cancel a complete info tasks
     * @param taskId    : a string with the task id
     * @param userInfo  : an AuthenticatedRequest object with user info
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     */
    @Transactional
    public void cancelCompleteInfoTask(String taskId, AuthenticatedRequest userInfo) {
        caseSecurityService.checkEditTask(userInfo.getRequester(), taskId);
        // It's important to set the new variable before retrieving them all
        taskService.setVariable(taskId, COMPLETE_INFO_ACTION, VALUE_REQUEST_FOR_CANCELATION);
        taskService.setVariable(taskId,COMPLETE_INFO_COMMENT, null);
        Map<String, Object> taskVariables = taskUtils.getTaskVariablesCompleteInfo(taskId);
        taskUtils.completeTaskCompleteInfo(taskId, taskVariables, userInfo.getRequester().getUser(),
                FieldConstants.VALUE_COMPLETE);
    }


}
