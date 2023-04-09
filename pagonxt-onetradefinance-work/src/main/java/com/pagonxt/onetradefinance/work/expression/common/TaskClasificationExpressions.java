package com.pagonxt.onetradefinance.work.expression.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.pagonxt.onetradefinance.work.common.ExtensionElementsConstants;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * class for Task Classification expressions
 * @author -
 * @version jdk-11.0.13
 * @see org.flowable.engine.TaskService
 * @since jdk-11.0.13
 */
@Service
public class TaskClasificationExpressions {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(TaskClasificationExpressions.class);

    //Class attribute
    private final TaskService taskService;

    /**
     * Constructor method
     * @param taskService : a TaskService object
     */
    public TaskClasificationExpressions(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * class method to set the label for task classification
     * @param execution         : a DelegateExecution object
     * @param task              : a TaskEntity object
     * @param jsonArrayString   : a string object
     * @see org.flowable.engine.delegate.DelegateExecution
     * @see org.flowable.task.service.impl.persistence.entity
     */
    public void setTaskClasificationLabels(DelegateExecution execution, TaskEntity task, String jsonArrayString){
        JsonMapper mapper = new JsonMapper();
        try {
            JsonNode jsonNode = mapper.readTree(jsonArrayString);
            taskService.setVariableLocal(task.getId(),
                    ExtensionElementsConstants.USER_TASK_LABELS_EXTENSION_ELEMENT_VARIABLE_NAME,jsonNode);
        } catch (JsonProcessingException e) {
            LOG.error("Error processing json array string", e);
            throw new PagoNxtExpressionException(String.format("Error processing json array string." +
                    " The task does not contain a valid tag list: %s", jsonArrayString));
        }
    }
}
