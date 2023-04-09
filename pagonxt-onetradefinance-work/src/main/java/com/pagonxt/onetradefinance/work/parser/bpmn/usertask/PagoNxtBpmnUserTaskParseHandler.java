package com.pagonxt.onetradefinance.work.parser.bpmn.usertask;

import com.pagonxt.onetradefinance.work.common.ExtensionElementsConstants;
import net.minidev.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.flowable.bpmn.model.*;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.parse.BpmnParseHandler;
import org.springframework.boot.configurationprocessor.json.JSONArray;

import java.util.*;
import java.util.stream.Collectors;

import static org.flowable.task.service.delegate.BaseTaskListener.*;

/**
 * BPMN parse handler for Pagonxt User Tasks
 * @author -
 * @version jdk-11.0.13
 * @see org.flowable.engine.parse.BpmnParseHandler
 * @since jdk-11.0.13
 */
public class PagoNxtBpmnUserTaskParseHandler implements BpmnParseHandler {

    protected static final String DISALLOWED_USER_EXPRESSION = "disallowedUserExpression";

    protected static final String COMPLETION_USER_VARIABLE = "completionUserVariable";

    protected static final String EXTERNAL_TASK_VARIABLE = "isExternalTask";

    protected static final String EXTERNAL_TASK_TYPE_VARIABLE = "externalTaskType";

    protected static final String CATEGORY_DEFERED = "DEFERED_";

    protected static final String EXTERNAL_TASK_TYPE_DRAFT = "EXTERNAL_REQUEST_DRAFT";

    /**
     * Method to get handled types
     * @see org.flowable.bpmn.model.BaseElement
     * @return a Collection object
     */
    @Override
    public Collection<Class<? extends BaseElement>> getHandledTypes() {
        Set<Class<? extends BaseElement>> types = new HashSet<>();
        types.add(UserTask.class);
        return types;
    }

    /**
     * Class method to parse
     * @param bpmnParse     : a BpmnParse object
     * @param baseElement   : a BaseElement object
     * @see org.flowable.bpmn.model.BaseElement
     * @see org.flowable.engine.impl.bpmn.parser.BpmnParse
     */
    @Override
    public void parse(BpmnParse bpmnParse, BaseElement baseElement) {
        UserTask userTask = (UserTask) baseElement;

        processCompletionUserVariableProperty(userTask);
        processDisallowedUserExpressionProperty(userTask);
        createOnCreateTaskListener(userTask);
        processCustomClasificationExtensionElements(userTask);

        setStartOnHoldListener(userTask);
        setEndOnHoldListener(userTask);
    }

    /**
     * Method to process the property of completion user variable
     * @param userTask : an UserTask object
     */
    private void processCompletionUserVariableProperty(UserTask userTask) {
        String completionUserVariable = getCompletionUserVariable(userTask);
        if (completionUserVariable != null) {
            createOnCompleteTaskListener(userTask, completionUserVariable);
        }
    }

    /**
     * Method to get the completion user variable
     * @param userTask : an UserTask object
     * @return a string object with the completion user variable
     */
    private String getCompletionUserVariable(UserTask userTask) {
        List<ExtensionElement> completionUserVariableList =
                userTask.getExtensionElements().get(COMPLETION_USER_VARIABLE);
        if (CollectionUtils.isNotEmpty(completionUserVariableList)) {
            return completionUserVariableList.get(0).getElementText();
        }
        return null;
    }

    /**
     * Method to create a listener on complete task
     * @param userTask              :an UserTask object
     * @param completionUserVariable: a string with the completion user variable
     */
    private void createOnCompleteTaskListener(UserTask userTask, String completionUserVariable) {
        FlowableListener createListener = new FlowableListener();
        createListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        createListener.setImplementation("${setCompletionUserExpression.addCompletionUser(execution, task, \""
                + completionUserVariable + "\")}");
        createListener.setEvent(EVENTNAME_COMPLETE);
        userTask.getTaskListeners().add(createListener);
    }

    /**
     * Method to process an expression property for disallowed users
     * @param userTask :an UserTask object
     */
    private void processDisallowedUserExpressionProperty(UserTask userTask) {
        String disallowedUserExpression = getDisallowedUserExpression(userTask);
        if (disallowedUserExpression != null) {
            createOnAssignmentTaskListener(userTask, disallowedUserExpression);
        }
    }

    /**
     * Method to get a disallowed user expression
     * @param userTask : an UserTask object
     * @return a string object with the disallowed user expression
     */
    private String getDisallowedUserExpression(UserTask userTask) {
        List<ExtensionElement> disallowedUserVariableList = userTask
                .getExtensionElements().get(DISALLOWED_USER_EXPRESSION);
        if (CollectionUtils.isNotEmpty(disallowedUserVariableList)) {
            String elementText = disallowedUserVariableList.get(0).getElementText();

            if (elementText.startsWith("${") && elementText.endsWith("}")) {
                return elementText.substring(2, elementText.length() - 1);
            } else {
                throw new BpmnUserTaskParserException("disallowedUserExpression must be a valid backend expression");
            }

        }
        return null;
    }

    /**
     * Method to create a listener on assignment task
     * @param userTask                  : an UserTask object
     * @param disallowedUserExpression  : a string with the disallowed user expression
     */
    private void createOnAssignmentTaskListener(UserTask userTask, String disallowedUserExpression) {
        FlowableListener createListener = new FlowableListener();
        createListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        createListener.setImplementation("${claimDisallowedExpression.disableClaimForUser(execution, task, "
                + disallowedUserExpression + ")}");
        createListener.setEvent(EVENTNAME_ASSIGNMENT);
        userTask.getTaskListeners().add(createListener);
    }

    /**
     * Method to create a listener on create task
     * @param userTask : an UserTask object
     */
    private void createOnCreateTaskListener(UserTask userTask) {
        FlowableListener createListener = new FlowableListener();
        createListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        createListener.setImplementation("${taskPermissionExpressions.resolvePermissions(execution,task)}");
        createListener.setEvent(EVENTNAME_CREATE);
        userTask.getTaskListeners().add(createListener);
    }

    /**
     * Method to process custom classification extension elements
     * @param userTask : an UserTask object
     */
    private void processCustomClasificationExtensionElements(UserTask userTask) {
        //Read all userTask extension elements
        Map<String, List<ExtensionElement>> extensionElements = userTask.getExtensionElements();
        Set<String> extensionKeys = extensionElements.keySet();
        //Process extension elements form catetory levels
        Set<String> customClasificationExtensionKeySet =
                new HashSet<>(ExtensionElementsConstants.CATEGORY_LEVEL_EXTENSION_ELEMENT_KEYS);
        Set<String> filteredExtensionKeys = extensionKeys.stream()
                .filter(key -> customClasificationExtensionKeySet.contains(key)).collect(Collectors.toSet());
        for (String varName : filteredExtensionKeys){
            String varValue = extensionElements.get(varName).get(0).getElementText();
            if (!varValue.isBlank()){
                createOnCreateTaskListenerForAddTaskClasificationVariable (userTask, varName, varValue );
            }
        }
        //Process extension element for user task labels
        if (extensionKeys.contains(ExtensionElementsConstants.USER_TASK_LABELS_EXTENSION_ELEMENT_KEY)){
            String varValue = getLabels(extensionElements
                    .get(ExtensionElementsConstants.USER_TASK_LABELS_EXTENSION_ELEMENT_KEY));
            createOnCreateTaskListenerForAddTaskClasificationLabels(userTask,varValue);
        }
    }

    /**
     * Method to create a listener on create task for add a task classification variables
     * @param userTask                  : an UserTask object
     * @param clasificationVariableName : a string with the name of the clasification variable
     * @param variableValue             : a string with the variable value
     */
    private void createOnCreateTaskListenerForAddTaskClasificationVariable(
            UserTask userTask, String clasificationVariableName, String variableValue) {
        FlowableListener createListener = new FlowableListener();
        createListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        //Example expression for add a new variable to a task:
        // ${taskService.setVariable(task.id, "categoryL1","REVIEW")}
        String listenerString = "${taskService.setVariableLocal(task.id, \"" +
                clasificationVariableName + "\", \"" + variableValue +"\")}";
        createListener.setImplementation(listenerString);
        createListener.setEvent(EVENTNAME_CREATE);
        userTask.getTaskListeners().add(createListener);
    }

    /**
     * Method to get labels
     * @param extensionElementList : a list with extension elements
     * @return a string object with labels
     */
    private String getLabels(List<ExtensionElement> extensionElementList){
        List<String> labelValuesList = new ArrayList<>();
        for (ExtensionElement extElement : extensionElementList){
            labelValuesList.add(extElement.getAttributes().get(ExtensionElementsConstants
                    .USER_TASK_LABELS_EXTENSION_ELEMENT_NAME).get(0).getValue());
        }
        JSONArray jsArray = new JSONArray(labelValuesList);
        return JSONObject.escape(jsArray.toString());
    }

    /**
     * Method to create a listener on create task for add task classification labels
     * @param userTask          : an UserTask object
     * @param jsonArrayString   : a string object
     */
    private void createOnCreateTaskListenerForAddTaskClasificationLabels(UserTask userTask, String jsonArrayString) {
        FlowableListener createListener = new FlowableListener();
        createListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        //Example expression for add a new variable to a task:
        // ${taskService.setVariable(task.id, "categoryL1","REVIEW")}
        String listenerString = "${taskClasificationExpressions.setTaskClasificationLabels(execution, task, \"" +
                jsonArrayString + "\")}";
        createListener.setImplementation(listenerString);
        createListener.setEvent(EVENTNAME_CREATE);
        userTask.getTaskListeners().add(createListener);
    }

    /**
     * Method to set start on hold listener
     * @param userTask: an UserTask object
     */
    private void setStartOnHoldListener(UserTask userTask) {
        if (isOnHoldTask(userTask)) {
            createOnHoldStartListener(userTask);
        }
    }

    /**
     * Method to create on hold start listener
     * @param userTask : an UserTask object
     */
    private void createOnHoldStartListener(UserTask userTask) {
        FlowableListener createListener = new FlowableListener();
        createListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        createListener.setImplementation("${slaExpressions.setStartOnHold(root.id)}");
        createListener.setEvent(EVENTNAME_CREATE);
        userTask.getTaskListeners().add(createListener);
    }

    /**
     * Method to set end on hold listener
     * @param userTask: an UserTask object
     */
    private void setEndOnHoldListener(UserTask userTask) {
        if (isOnHoldTask(userTask)) {
            createMinutesOnHoldListener(userTask);
        }
    }

    /**
     * Method to create minutes on hold listener
     * @param userTask: an UserTask object
     */
    private void createMinutesOnHoldListener(UserTask userTask) {
        FlowableListener createListener = new FlowableListener();
        createListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        createListener.setImplementation("${slaExpressions.updateMinutesOnHold(root.id)}");
        createListener.setEvent(EVENTNAME_COMPLETE);
        userTask.getTaskListeners().add(createListener);
    }

    /**
     * Method to check if a task is on hold
     * @param userTask: an UserTask object
     * @return true or false if the task is on hold
     */
    private boolean isOnHoldTask(UserTask userTask) {
        // Option 1: External task (except Draft task)
        List<ExtensionElement> externalTaskVariableList = userTask.getExtensionElements().get(EXTERNAL_TASK_VARIABLE);
        if (CollectionUtils.isNotEmpty(externalTaskVariableList) && "true"
                .equals(externalTaskVariableList.get(0).getElementText())) {
            List<ExtensionElement> externalTaskTypeVariableList = userTask
                    .getExtensionElements().get(EXTERNAL_TASK_TYPE_VARIABLE);
            if (CollectionUtils.isNotEmpty(externalTaskVariableList)) {
                return !externalTaskTypeVariableList.get(0).getElementText().equals(EXTERNAL_TASK_TYPE_DRAFT);
            }
        }
        // Option 2: Tasks after defer
        return userTask.getCategory() != null && userTask.getCategory().startsWith(CATEGORY_DEFERED);
    }
}
