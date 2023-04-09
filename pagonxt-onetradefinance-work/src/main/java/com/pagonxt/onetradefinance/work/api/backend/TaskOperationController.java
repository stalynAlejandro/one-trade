package com.pagonxt.onetradefinance.work.api.backend;

import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.work.service.TaskOperationService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for tasks operations
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.TaskOperationService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping(value = "/backend/task-operation", produces = "application/json")
public class TaskOperationController {

    //class attribute
    private final TaskOperationService taskOperationService;

    /**
     * constructor method
     * @param taskOperationService Service that provides necessary functionality to this controller
     */
    public TaskOperationController(TaskOperationService taskOperationService) {
        this.taskOperationService = taskOperationService;
    }

    /**
     * Method to cancel a complete info task
     * @param taskId   : a string with the task id. It's required a "complete information" task.
     * @param userInfo : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PutMapping("/complete-info/cancel/{taskId}")
    public ControllerResponse cancelCompleteInfoTask(
            @PathVariable String taskId, @RequestBody AuthenticatedRequest userInfo) {
        taskOperationService.cancelCompleteInfoTask(taskId, userInfo);
        return ControllerResponse.success("cancelCompleteInfoTask", null);
    }
}
