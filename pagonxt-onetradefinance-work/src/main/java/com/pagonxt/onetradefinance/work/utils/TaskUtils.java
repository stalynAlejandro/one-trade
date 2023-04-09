package com.pagonxt.onetradefinance.work.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.platform.service.task.CompleteFormRepresentation;
import com.flowable.platform.service.task.PlatformTaskService;
import com.flowable.platform.service.task.TaskRepresentation;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.service.model.Task;
import com.pagonxt.onetradefinance.work.service.model.Variable;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.engine.TaskService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;

/**
 * Class with task utils
 * @author -
 * @version jdk-11.0.13
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see org.flowable.engine.TaskService
 * @see com.flowable.platform.service.task.PlatformTaskService
 * @see org.flowable.cmmn.api.CmmnRuntimeService
 * @since jdk-11.0.13
 */
@Component
public class TaskUtils {

    private static final List<String> INTEGER_NUMERIC_TYPES = List.of("integer", "long");
    private static final List<String> DECIMAL_NUMERIC_TYPES = List.of("float", "double");
    public static final String SCOPE_ROOT = "root";
    public static final String VARIABLE_PREFIX = "variables.";
    public static final String COMPLETE_INFORMATION = "COMPLETE_INFORMATION";

    //class attributes
    private final ObjectMapper mapper;
    private final TaskService taskService;
    private final PlatformTaskService platformTaskService;
    private final CmmnRuntimeService cmmnRuntimeService;

    /**
     * constructor method
     * @param mapper                : an ObjectMapper object
     * @param taskService           : a TaskService object
     * @param platformTaskService   : a PlatformTaskService object
     * @param cmmnRuntimeService    : a CmmnRuntimeService object
     */
    public TaskUtils(ObjectMapper mapper,
                     TaskService taskService,
                     PlatformTaskService platformTaskService,
                     CmmnRuntimeService cmmnRuntimeService) {
        this.mapper = mapper;
        this.taskService = taskService;
        this.platformTaskService = platformTaskService;
        this.cmmnRuntimeService = cmmnRuntimeService;
    }

    /**
     * Method to complete a draft task
     * @param caseInstance  : a CaseInstance object
     * @param requesterUser : a User object with the requester user
     * @see org.flowable.cmmn.api.runtime.CaseInstance
     * @see com.pagonxt.onetradefinance.integrations.model.User
     */
    public void completeTaskDraft(CaseInstance caseInstance, User requesterUser) {
        String taskId = taskService.createTaskQuery()
                .includeTaskLocalVariables()
                .caseInstanceIdWithChildren(caseInstance.getId())
                .list().stream()
                .filter(task -> Boolean.TRUE.toString().equals(task.getTaskLocalVariables().get(IS_EXTERNAL_TASK)))
                .filter(task -> "EXTERNAL_REQUEST_DRAFT".equals(task.getTaskLocalVariables().get(EXTERNAL_TASK_TYPE)))
                .collect(toSingleton())
                .getId();
        taskService.setVariableLocal(taskId, TASK_COMPLETION_USER_ID, requesterUser.getUserId());
        taskService.setVariableLocal(taskId, TASK_COMPLETION_USER_DISPLAYED_NAME, requesterUser.getUserDisplayedName());
        taskService.complete(taskId);
        // Delete data no longer necessary
        cmmnRuntimeService.removeVariable(caseInstance.getId(), REQUEST_SAVED_STEP);
        cmmnRuntimeService.removeVariable(caseInstance.getId(), REQUEST_DOCUMENTS);
    }

    /**
     * Method to complete a complete info task
     *
     * @param taskId        : a string object with the task id
     * @param taskVariables : a collection of task variables
     * @param requesterUser : a User object with the requester user
     * @param formOutcome   : a string whit the value of the form outcome
     * @see com.pagonxt.onetradefinance.integrations.model.User
     */
    public void completeTaskCompleteInfo(String taskId, Map<String, Object> taskVariables,
                                         User requesterUser, String formOutcome) {
        Map<String, Object> reMappedTaskVariables = reMap(taskVariables);
        CompleteFormRepresentation platformFormRepresentation = new CompleteFormRepresentation();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(formOutcome)){
            platformFormRepresentation.setOutcome(formOutcome);
        }
        for(Map.Entry<String, Object> variable : reMappedTaskVariables.entrySet()) {
            platformFormRepresentation.setValues(variable.getKey(), variable.getValue());
        }
        taskService.setVariableLocal(taskId, TASK_COMPLETION_USER_ID, requesterUser.getUserId());
        taskService.setVariableLocal(taskId, TASK_COMPLETION_USER_DISPLAYED_NAME, requesterUser.getUserDisplayedName());
        platformTaskService.completeTaskForm(taskId, platformFormRepresentation);
    }

    /**
     * Method to get a task
     * @param taskId        : a string with the task id
     * @param translation   : a boolean value
     * @see com.flowable.platform.service.task.TaskRepresentation
     * @return a TaskRepresentation object, with the task
     */
    public TaskRepresentation getTask(String taskId, boolean translation) {
        if (Strings.isBlank(taskId)) {
            throw new InvalidRequestException("The field 'taskId' must be informed.", "errorTaskId");
        }
        TaskRepresentation task = platformTaskService.getTask(taskId, translation);
        if(task == null) {
            throw new ResourceNotFoundException(String.format("No task found with id %s", taskId), "errorTaskNotFound");
        }
        return task;
    }

    /**
     * Method to get variables from a complete info task
     * @param taskId : a string object with the task id
     * @return a collection with task variables
     */
    public Map<String, Object> getTaskVariablesCompleteInfo(String taskId) {
        if (Strings.isBlank(taskId)) {
            throw new InvalidRequestException("The field 'taskId' must be informed.", "errorTaskId");
        }
        Map<String, Object> taskVariables = platformTaskService.getTaskVariables(taskId);
        if(taskVariables == null) {
            throw new ResourceNotFoundException(String.format("No task found with id %s", taskId), "errorTaskNotFound");
        }
        if(!Boolean.TRUE.toString().equals(taskVariables.get(IS_EXTERNAL_TASK))  ||
                !COMPLETE_INFORMATION.equals(taskVariables.get(EXTERNAL_TASK_TYPE))) {
            throw new InvalidRequestException(String.format("The task found with id %s is not" +
                    " the right type", taskId), "errorInvalidTask");
        }
        return taskVariables;
    }


    /**
     * class method to remap variables
     * This re-mapping allows to map all the variables to the desired format, particularly dates.
     * @param map : a map collection
     * @return a map collection
     */
    private Map<String, Object> reMap(Map<String, Object> map) {
        TypeReference<Map<String,Object>> typeRef = new TypeReference<>() {};
        Map<String, Object> result;
        try {
            String jsonString = mapper.writeValueAsString(map);
            result = mapper.readValue(jsonString, typeRef);

        } catch (JsonProcessingException e) {
            throw new ServiceException("Processing error mapping variables", "errorMappingVariables",  e);
        }
        // Don't re-map operation
        Map<String, Object> root = (Map<String, Object>) map.get(ROOT);
        Map<String, Object> rootReMapped = (Map<String, Object>) result.get(ROOT);
        if(root != null) {
            rootReMapped.put(OPERATION, root.get(OPERATION));
        }
        return result;
    }

    /**
     * Method to set variables of a complete info task
     * @param taskId            : a string object with the task id
     * @param comment           : a string with the comment
     * @param requestVariables  : a collection with the request variables
     * @return a map collection with task variables
     */
    public Map<String, Object> setCompleteInfoTaskVariables(String taskId,
                                                            String comment,
                                                            Map<String, Object> requestVariables) {
        taskService.setVariable(taskId, COMPLETE_INFO_ACTION, VALUE_CONTINUE);
        taskService.setVariable(taskId,COMPLETE_INFO_COMMENT, comment);
        Map<String, Object> taskVariables = getTaskVariablesCompleteInfo(taskId);
        // Update DataObject
        Map<String, Object> operation =
                (Map<String, Object>) ((Map<String, Object>) taskVariables.get(SCOPE_ROOT)).get(OPERATION);

        for(Map.Entry<String, Object> variable : requestVariables.entrySet()) {
            // TODO : Modificar sólo los datos de la operación que este
            //  permitido modificar (pendiente validar con cliente)
            operation.put(variable.getKey(), variable.getValue());
        }
        return taskVariables;
    }

    /**
     * class method
     * @param instance : a Task object
     * @param name     : a string object with name
     * @see com.pagonxt.onetradefinance.work.service.model.Task
     * @return  a string object
     */
    public static String getString(Task instance, String name) {
        return getString(instance, name, null);
    }

    /**
     * class method
     * @param instance : a Task object
     * @param name     : a string object with name
     * @see com.pagonxt.onetradefinance.work.service.model.Task
     * @return  a Double object
     */
    public static Double getDouble(Task instance, String name) {
        return getDouble(instance, name, null);
    }

    /**
     * class method
     * @param instance : a Task object
     * @param name     : a string object with name
     * @see com.pagonxt.onetradefinance.work.service.model.Task
     * @return  an Integer object
     */
    public static Integer getInteger(Task instance, String name) {
        return getInteger(instance, name, null);
    }

    /**
     * class method
     * @param instance : a Task object
     * @param name     : a string object with name
     * @see com.pagonxt.onetradefinance.work.service.model.Task
     * @return  a Date object
     */
    public static Date getDate(Task instance, String name) {
        return getDate(instance, name, null);
    }

    /**
     * class method
     * @param instance              : a Task object
     * @param name                  : a string object with name
     * @param scopeHierarchyType    : a string object with the scope hierarchy type
     * @see com.pagonxt.onetradefinance.work.service.model.Task
     * @return  a string object
     */
    public static String getString(Task instance, String name, String scopeHierarchyType) {
        return instance.getVariables().parallelStream()
                .filter(variable -> equalsNameAndHierarchy(variable, name, scopeHierarchyType) &&
                        "string".equals(variable.getType()))
                .map(Variable::getTextValue)
                .findFirst().orElse(null);
    }

    /**
     * class method
     * @param instance              : a Task object
     * @param name                  : a string object with name
     * @param scopeHierarchyType    : a string object with the scope hierarchy type
     * @see com.pagonxt.onetradefinance.work.service.model.Task
     * @return  a Double object
     */
    public static Double getDouble(Task instance, String name, String scopeHierarchyType) {
        return instance.getVariables().parallelStream()
                .filter(variable -> equalsNameAndHierarchy(variable, name, scopeHierarchyType) &&
                        DECIMAL_NUMERIC_TYPES.contains(variable.getType()) &&
                        StringUtils.hasText(variable.getRawValue()))
                .map(variable -> Double.parseDouble(variable.getRawValue()))
                .findFirst().orElse(null);
    }

    /**
     * class method
     * @param instance              : a Task object
     * @param name                  : a string object with name
     * @param scopeHierarchyType    : a string object with the scope hierarchy type
     * @see com.pagonxt.onetradefinance.work.service.model.Task
     * @return  an Integer object
     */
    public static Integer getInteger(Task instance, String name, String scopeHierarchyType) {
        return instance.getVariables().parallelStream()
                .filter(variable -> equalsNameAndHierarchy(variable, name, scopeHierarchyType) &&
                        INTEGER_NUMERIC_TYPES.contains(variable.getType()) &&
                        StringUtils.hasText(variable.getRawValue()))
                .map(variable -> Integer.parseInt(variable.getRawValue()))
                .findFirst().orElse(null);
    }

    /**
     * class method
     * @param instance              : a Task object
     * @param name                  : a string object with name
     * @param scopeHierarchyType    : a string object with the scope hierarchy type
     * @see com.pagonxt.onetradefinance.work.service.model.Task
     * @return  a Date object
     */
    public static Date getDate(Task instance, String name, String scopeHierarchyType) {
        return instance.getVariables().parallelStream()
                .filter(variable -> equalsNameAndHierarchy(variable, name, scopeHierarchyType) &&
                        "date".equals(variable.getType()) &&
                        StringUtils.hasText(variable.getRawValue()))
                .map(Variable::getDateValue)
                .findFirst().orElse(null);
    }


    /**
     * class method
     * @param variable     : a Variable object
     * @param name         : a string object with name
     * @param hierarchy    : a string object with hierarchy
     * @see com.pagonxt.onetradefinance.work.service.model.Variable
     * @return  a boolean value
     */
    private static boolean equalsNameAndHierarchy(Variable variable, String name, String hierarchy) {
        return name.equals(variable.getName()) &&
                (hierarchy == null || hierarchy.equals(variable.getScopeHierarchyType()));
    }

    /**
     * class method
     * @param <T> : a generic collection
     * @return a generic collection
     */
    private <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

    /**
     * method to generate a var name
     * @param field : a string object with the field
     * @return : a string object with the var name
     */
    public static String generateVarName(String field) {
        return VARIABLE_PREFIX + field;
    }
}
