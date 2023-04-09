package com.pagonxt.onetradefinance.work.serializer;

import com.pagonxt.onetradefinance.integrations.model.historictask.PagoNxtHistoricTaskItem;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.model.Task;
import com.pagonxt.onetradefinance.work.service.model.Translation;
import com.pagonxt.onetradefinance.work.service.model.Variable;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UnitTest
class PagoNxtHistoricTaskItemSerializerTest {

    @InjectMocks
    private PagoNxtHistoricTaskItemSerializer pagoNxtHistoricTaskItemSerializer;

    @Test
    void serialize_whenTask_returnsPagoNxtHistoricTaskItem() {
        // Given
        Task task = new Task();
        Date date1 = new Date();
        task.setCreateTime(date1);
        Date date2 = new Date();
        task.setEndTime(date2);
        Variable variableIsExternalTask = new Variable();
        variableIsExternalTask.setName("isExternalTask");
        variableIsExternalTask.setScopeHierarchyType(null);
        variableIsExternalTask.setType("string");
        variableIsExternalTask.setTextValue("true");
        Variable variableTaskCompletion = new Variable();
        variableTaskCompletion.setName("taskCompletionUserDisplayedName");
        variableTaskCompletion.setScopeHierarchyType(null);
        variableTaskCompletion.setType("string");
        String userName = "userNameTest";
        variableTaskCompletion.setTextValue(userName);
        task.setVariables(List.of(variableIsExternalTask, variableTaskCompletion));
        Translation translation = new Translation();
        translation.setKey("name");
        translation.setValue("Nombre de la tarea | CLE-TEST");
        translation.setLocale("es_es");
        task.setTranslations(List.of(translation));
        // When
        PagoNxtHistoricTaskItem result = pagoNxtHistoricTaskItemSerializer.serialize(task, "es_es");
        // Then
        assertEquals(date1, result.getStartDate());
        assertEquals(date2, result.getEndDate());
        assertEquals("Nombre de la tarea", result.getTaskName());
        assertEquals(userName, result.getUserName());
    }

    @Test
    void serialize_whenNullTask_returnsNull() {
        // Given and When
        PagoNxtHistoricTaskItem result = pagoNxtHistoricTaskItemSerializer.serialize(null, "es_es");
        // Then
        assertNull(result);
    }

    @Test
    void serialize_whenNullTranslation_returnsDefaultTaskName() {
        // Given
        Task task = new Task();
        Date date1 = new Date();
        task.setCreateTime(date1);
        Date date2 = new Date();
        task.setEndTime(date2);
        Variable variableIsExternalTask = new Variable();
        variableIsExternalTask.setName("isExternalTask");
        variableIsExternalTask.setScopeHierarchyType(null);
        variableIsExternalTask.setType("string");
        variableIsExternalTask.setTextValue("true");
        Variable variableTaskCompletion = new Variable();
        variableTaskCompletion.setName("taskCompletionUserDisplayedName");
        variableTaskCompletion.setScopeHierarchyType(null);
        variableTaskCompletion.setType("string");
        String userName = "userNameTest";
        variableTaskCompletion.setTextValue(userName);
        task.setVariables(List.of(variableIsExternalTask, variableTaskCompletion));
        Translation translation = new Translation();
        translation.setKey("name");
        translation.setValue("Nombre de la tarea | CLE-TEST");
        translation.setLocale("es_es");
        task.setTranslations(List.of(translation));
        task.setName("Default task name | CLE-TEST");
        // When
        PagoNxtHistoricTaskItem result = pagoNxtHistoricTaskItemSerializer.serialize(task, null);
        // Then
        assertEquals(date1, result.getStartDate());
        assertEquals(date2, result.getEndDate());
        assertEquals("Default task name", result.getTaskName());
        assertEquals(userName, result.getUserName());
    }

    @Test
    void serialize_whenInternalTask_returnsBackOfficeUser() {
        // Given
        Task task = new Task();
        task.setVariables(Collections.emptyList());
        // When
        PagoNxtHistoricTaskItem result = pagoNxtHistoricTaskItemSerializer.serialize(task, "es_es");
        // Then
        assertEquals("Back Office", result.getUserName());
    }


    @Test
    void serialize_whenExternalTaskUserNameNull_returnsBackOfficeUser() {
        // Given
        Task task = new Task();
        Variable variableIsExternalTask = new Variable();
        variableIsExternalTask.setName("isExternalTask");
        variableIsExternalTask.setScopeHierarchyType(null);
        variableIsExternalTask.setType("string");
        variableIsExternalTask.setTextValue("true");
        task.setVariables(List.of(variableIsExternalTask));
        // When
        PagoNxtHistoricTaskItem result = pagoNxtHistoricTaskItemSerializer.serialize(task, "es_es");
        // Then
        assertEquals("Back Office", result.getUserName());
    }

    @Test
    void serialize_whenNoTranslation_returnsDefaultTaskName() {
        // Given
        Task task = new Task();
        task.setVariables(Collections.emptyList());
        task.setName("Default task name | CLE-TEST");
        // When
        PagoNxtHistoricTaskItem result = pagoNxtHistoricTaskItemSerializer.serialize(task, "es_es");
        // Then
        assertEquals("Default task name", result.getTaskName());
    }

    @Test
    void serialize_whenTaskNameWithoutSeparator_returnsSameTaskName() {
        // Given
        Task task = new Task();
        task.setVariables(Collections.emptyList());
        Translation translation = new Translation();
        translation.setKey("name");
        String taskName = "Nombre de la tarea";
        translation.setValue(taskName);
        translation.setLocale("es_es");
        task.setTranslations(List.of(translation));
        // When
        PagoNxtHistoricTaskItem result = pagoNxtHistoricTaskItemSerializer.serialize(task, "es_es");
        // Then
        assertEquals(taskName, result.getTaskName());
    }
}
