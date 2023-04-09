package com.pagonxt.onetradefinance.work.parser.bpmn.usertask;

import com.pagonxt.onetradefinance.work.common.ExtensionElementsConstants;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.flowable.bpmn.model.*;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.*;

import static com.pagonxt.onetradefinance.work.parser.bpmn.usertask.PagoNxtBpmnUserTaskParseHandler.COMPLETION_USER_VARIABLE;
import static com.pagonxt.onetradefinance.work.parser.bpmn.usertask.PagoNxtBpmnUserTaskParseHandler.DISALLOWED_USER_EXPRESSION;
import static org.flowable.task.service.delegate.BaseTaskListener.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
class PagoNxtBpmnUserTaskParseHandlerTest {

    @InjectMocks
    PagoNxtBpmnUserTaskParseHandler pagoNxtBpmnUserTaskParseHandler;

    @Test
    void getHandledTypes_isOk_hasUserTaskHandledType() {
        //given

        //when
        Collection<Class<? extends BaseElement>> handledTypes = pagoNxtBpmnUserTaskParseHandler.getHandledTypes();

        //then
        Set<Class<? extends BaseElement>> expected = Set.of(UserTask.class);
        assertEquals(expected, handledTypes);
    }

    @Test
    void parse_noPropertiesConfigured_noListenersAdded() {
        //given
        BpmnParse bpmnParse = mock(BpmnParse.class);
        UserTask userTask = mock(UserTask.class);

        //when
        pagoNxtBpmnUserTaskParseHandler.parse(bpmnParse, userTask);

        //then
        assertEquals(0, userTask.getTaskListeners().size(), "There should be no listeners added to the user task");
    }

    @Test
    void parse_disallowedUserExpressionProperty_onAssignmentListenerAdded() {
        //given
        ExtensionElement extensionElement = mock(ExtensionElement.class);
        when(extensionElement.getElementText()).thenReturn("${disallowedUser}");
        UserTask userTask = mock(UserTask.class);
        when(userTask.getExtensionElements()).thenReturn(Map.of(DISALLOWED_USER_EXPRESSION, List.of(extensionElement)));
        when(userTask.getTaskListeners()).thenReturn(new ArrayList<>());
        BpmnParse bpmnParse = mock(BpmnParse.class);

        //when
        pagoNxtBpmnUserTaskParseHandler.parse(bpmnParse, userTask);

        //then
        assertTrue(userTask.getTaskListeners().stream().anyMatch(item -> EVENTNAME_ASSIGNMENT.equals(item.getEvent())), "There should be at least one task listener whith event: assigment");
    }

    @Test
    void parse_disallowedUserExpressionPropertyNotCorrect_throwsException() {
        //given
        BpmnParse bpmnParse = mock(BpmnParse.class);
        UserTask userTask = mock(UserTask.class);
        ExtensionElement extensionElement = mock(ExtensionElement.class);
        doReturn("disallowedUser").when(extensionElement).getElementText();
        when(userTask.getExtensionElements()).thenReturn(Map.of(DISALLOWED_USER_EXPRESSION, List.of(extensionElement)));

        //when and then
        assertThrows(BpmnUserTaskParserException.class, () -> pagoNxtBpmnUserTaskParseHandler.parse(bpmnParse, userTask),"Should throw BpmnUserTaskParserException when disallowedUserExpression property is incorrect");
    }

    @Test
    void parse_completionUserVariableProperty_onCompleteListenerAdded() {
        //given
        BpmnParse bpmnParse = mock(BpmnParse.class);
        UserTask userTask = mock(UserTask.class);
        ExtensionElement extensionElement = mock(ExtensionElement.class);
        doReturn("taskOneCompletionUser").when(extensionElement).getElementText();
        when(userTask.getExtensionElements()).thenReturn(Map.of(COMPLETION_USER_VARIABLE, List.of(extensionElement)));
        when(userTask.getTaskListeners()).thenReturn(new ArrayList<>());

        //when
        pagoNxtBpmnUserTaskParseHandler.parse(bpmnParse, userTask);

        //then
       assertTrue(userTask.getTaskListeners().stream().anyMatch(item -> EVENTNAME_COMPLETE.equals(item.getEvent())),"There should be at least one valid task listener (event: complete)");
    }
    @Test
    void parse_createOnCreateTaskListener_onCreateListenerAdded() {
        //given
        BpmnParse bpmnParse = mock(BpmnParse.class);
        UserTask userTask = mock(UserTask.class);
        when(userTask.getTaskListeners()).thenReturn(new ArrayList<>());
        //when
        pagoNxtBpmnUserTaskParseHandler.parse(bpmnParse, userTask);

        //then
        assertTrue(userTask.getTaskListeners().stream().anyMatch(item -> EVENTNAME_CREATE.equals(item.getEvent())),"There should be at least one valid task listener (event: create)");
    }

    @Test
    void parse_processCustomClasificationExtensionElements_Category_onCreateListenerAdded() {
        String configuredCategory = "DUMMY_CATEGORY";
        //given
        BpmnParse bpmnParse = mock(BpmnParse.class);
        UserTask userTask = mock(UserTask.class);

        ExtensionElement categoryExtensionElement = new ExtensionElement();
        categoryExtensionElement.setName(ExtensionElementsConstants.CATEGORY_LEVEL_EXTENSION_ELEMENT_KEYS.get(0));
        categoryExtensionElement.setElementText(configuredCategory);
        doReturn(Map.of(ExtensionElementsConstants.CATEGORY_LEVEL_EXTENSION_ELEMENT_KEYS.get(0),List.of(categoryExtensionElement))).when(userTask).getExtensionElements();
        when(userTask.getTaskListeners()).thenReturn(new ArrayList<>());
        //when
        pagoNxtBpmnUserTaskParseHandler.parse(bpmnParse, userTask);

        //then
        assertTrue(userTask.getTaskListeners().stream().anyMatch(item -> EVENTNAME_CREATE.equals(item.getEvent())
                && item.getImplementation().contains(ExtensionElementsConstants.CATEGORY_LEVEL_EXTENSION_ELEMENT_KEYS.get(0))
                && item.getImplementation().contains(configuredCategory)),"There should be as least one task listener on create event containing the configured category 'DUMMY_CATEGORY");
    }
    @Test
    void parse_processCustomClasificationExtensionElements_Tags_onCreateListenerAdded() {
        String[] tagsStr=new String[] {"RED", "XL"};

        String listenerString = "${taskClasificationExpressions.setTaskClasificationLabels(execution, task, \"[\\\"RED\\\",\\\"XL\\\"]\")}";
        //given
        BpmnParse bpmnParse = mock(BpmnParse.class);
        UserTask userTask = mock(UserTask.class);

        //Create a list of extension elements for each tag. The extension element must be set "name" and attributes
        List<ExtensionElement> tagExtensionElementList = createTagExtensionElementList(tagsStr);

        //Set extension element for user task tags
        Map<String, List<ExtensionElement>> extensionElementsMap = new HashMap<String, List<ExtensionElement>>();
        extensionElementsMap.put(ExtensionElementsConstants.USER_TASK_LABELS_EXTENSION_ELEMENT_KEY, tagExtensionElementList);
        doReturn(extensionElementsMap).when(userTask).getExtensionElements();

        List<FlowableListener> taskListeners = new ArrayList<>();
        doReturn(taskListeners).when(userTask).getTaskListeners();
        //when
        pagoNxtBpmnUserTaskParseHandler.parse(bpmnParse, userTask);

        //then
        assertTrue(userTask.getTaskListeners().stream().anyMatch(item -> EVENTNAME_CREATE.equals(item.getEvent()) && item.getImplementation().contains(listenerString)),
                "There should be at least one task listener with event 'create' and implementation: " + listenerString);
    }

    private List<ExtensionElement> createTagExtensionElementList(String[] tagsStr){
        List<ExtensionElement> tagExtensionElementList = new ArrayList<ExtensionElement>();
        for(String tag : tagsStr){

            ExtensionAttribute extensionAttribute = new ExtensionAttribute();
            extensionAttribute.setName(ExtensionElementsConstants.USER_TASK_LABELS_EXTENSION_ELEMENT_NAME);
            extensionAttribute.setValue(tag);
            List<ExtensionAttribute> extensionAttributesList = new ArrayList<ExtensionAttribute>();
            extensionAttributesList.add(extensionAttribute);
            //Extension element with tag
            ExtensionElement tmpExtensionElement = new ExtensionElement();
            tmpExtensionElement.setName(ExtensionElementsConstants.USER_TASK_LABELS_EXTENSION_ELEMENT_KEY);
            Map<String, List<ExtensionAttribute>> attributesMap = new HashMap<String, List<ExtensionAttribute>>();
            attributesMap.put(ExtensionElementsConstants.USER_TASK_LABELS_EXTENSION_ELEMENT_NAME,extensionAttributesList);
            tmpExtensionElement.setAttributes(attributesMap);
            tagExtensionElementList.add(tmpExtensionElement);
        }
        return tagExtensionElementList;
    }

    @Test
    void parseCompleteInformationTask() {
        String listenerString = "${slaExpressions.updateMinutesOnHold(root.id)}";

        BpmnParse bpmnParse = mock(BpmnParse.class);
        UserTask userTask = mock(UserTask.class);

        ExtensionElement externalTaskExtension = mock(ExtensionElement.class);
        doReturn("true").when(externalTaskExtension).getElementText();
        ExtensionElement externalTaskTypeExtension = mock(ExtensionElement.class);
        doReturn("EXTERNAL_COMPLETE_INFO").when(externalTaskTypeExtension).getElementText();

        Map<String, List<ExtensionElement>> extensionElements = Map.of(
            "isExternalTask", List.of(externalTaskExtension),
            "externalTaskType", List.of(externalTaskTypeExtension));
        doReturn(extensionElements).when(userTask).getExtensionElements();

        List<FlowableListener> taskListeners = new ArrayList<>();
        doReturn(taskListeners).when(userTask).getTaskListeners();

        pagoNxtBpmnUserTaskParseHandler.parse(bpmnParse, userTask);

        assertTrue(userTask.getTaskListeners().stream()
                .anyMatch(item -> EVENTNAME_COMPLETE.equals(item.getEvent()) && item.getImplementation().contains(listenerString)),
            "There should be at least one task listener with event 'complete' and implementation: " + listenerString);
    }

    @Test
    void parseDraftTask() {
        String listenerString = "${slaExpressions.updateMinutesOnHold(root.id)}";

        BpmnParse bpmnParse = mock(BpmnParse.class);
        UserTask userTask = mock(UserTask.class);

        ExtensionElement externalTaskExtension = mock(ExtensionElement.class);
        doReturn("true").when(externalTaskExtension).getElementText();
        ExtensionElement externalTaskTypeExtension = mock(ExtensionElement.class);
        doReturn("EXTERNAL_REQUEST_DRAFT").when(externalTaskTypeExtension).getElementText();

        Map<String, List<ExtensionElement>> extensionElements = Map.of(
            "isExternalTask", List.of(externalTaskExtension),
            "externalTaskType", List.of(externalTaskTypeExtension));
        doReturn(extensionElements).when(userTask).getExtensionElements();

        List<FlowableListener> taskListeners = new ArrayList<>();
        doReturn(taskListeners).when(userTask).getTaskListeners();

        pagoNxtBpmnUserTaskParseHandler.parse(bpmnParse, userTask);

        assertTrue(userTask.getTaskListeners().stream()
            .noneMatch(item -> EVENTNAME_COMPLETE.equals(item.getEvent()) && item.getImplementation().contains(listenerString)),
            "There should not be any task listener with event 'complete' and implementation: " + listenerString);
    }

    @Test
    void parseReviewDeferedTask() {
        String listenerString = "${slaExpressions.updateMinutesOnHold(root.id)}";

        BpmnParse bpmnParse = mock(BpmnParse.class);
        UserTask userTask = mock(UserTask.class);

        doReturn("DEFERED_REVIEW").when(userTask).getCategory();

        List<FlowableListener> taskListeners = new ArrayList<>();
        doReturn(taskListeners).when(userTask).getTaskListeners();

        pagoNxtBpmnUserTaskParseHandler.parse(bpmnParse, userTask);

        assertTrue(userTask.getTaskListeners().stream()
                .anyMatch(item -> EVENTNAME_COMPLETE.equals(item.getEvent()) && item.getImplementation().contains(listenerString)),
            "There should be at least one task listener with event 'complete' and implementation: " + listenerString);
    }

    @Test
    void parseReviewTask() {
        String listenerString = "${slaExpressions.updateMinutesOnHold(root.id)}";

        BpmnParse bpmnParse = mock(BpmnParse.class);
        UserTask userTask = mock(UserTask.class);

        doReturn("REVIEW").when(userTask).getCategory();

        List<FlowableListener> taskListeners = new ArrayList<>();
        doReturn(taskListeners).when(userTask).getTaskListeners();

        pagoNxtBpmnUserTaskParseHandler.parse(bpmnParse, userTask);

        assertTrue(userTask.getTaskListeners().stream()
                .noneMatch(item -> EVENTNAME_COMPLETE.equals(item.getEvent()) && item.getImplementation().contains(listenerString)),
            "There should not be any task listener with event 'complete' and implementation: " + listenerString);
    }
}