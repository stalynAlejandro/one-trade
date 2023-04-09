package com.pagonxt.onetradefinance.work.security;

import com.flowable.dataobject.api.repository.serviceregistry.ServiceRegistryDataObjectModel;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.flowable.platform.service.task.PlatformTaskService;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.SecurityException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.work.config.TestUtils;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.service.model.Case;
import com.pagonxt.onetradefinance.work.service.model.Variable;
import com.pagonxt.onetradefinance.work.utils.CaseUtils;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static com.pagonxt.onetradefinance.work.common.CaseCommonConstants.VARNAME_PRODUCT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class CaseSecurityServiceImplTest {

    static CaseUtils caseUtils;

    static PlatformTaskService platformTaskService;

    static CaseSecurityService caseSecurityService;

    private void skipTestIfElasticLess() {
        TestUtils.skipTestIfSpringProfileActive("elasticless");
    }

    @BeforeAll
    static void setup() {
        caseUtils = mock(CaseUtils.class);
        platformTaskService = mock(PlatformTaskService.class);
        caseSecurityService = new CaseSecurityServiceImpl(caseUtils, platformTaskService);
    }

    @ParameterizedTest
    @MethodSource("privateCanReadAttributes")
    void privateCanRead_returnsValidResponse(UserInfo userInfo, String caseOffice, String caseMiddleOffice, Boolean expectedResult) {
        // When
        Boolean result = ReflectionTestUtils.invokeMethod(caseSecurityService, "canRead", userInfo, caseOffice, caseMiddleOffice);

        // Then
        assertEquals(expectedResult, result, "Result should match expected result");
    }

    @Test
    void privateCanRead_nullUserInfo_returnsFalse() {
        // When
        Boolean result = ReflectionTestUtils.invokeMethod(caseSecurityService, "canRead", null, null, null);

        // Then
        assertNotEquals(Boolean.TRUE, result, "Result should be false");
    }

    @Test
    void privateCanRead_noUser_returnsFalse() {
        // When
        Boolean result = ReflectionTestUtils.invokeMethod(caseSecurityService, "canRead", new UserInfo(null), null, null);

        // Then
        assertNotEquals(Boolean.TRUE, result, "Result should be false");
    }

    @Test
    void privateCanRead_noUserType_returnsFalse() {
        // When
        UserInfo userInfo = new UserInfo(new User("id", "displayName", null));
        Boolean result = ReflectionTestUtils.invokeMethod(caseSecurityService, "canRead", userInfo, null, null);

        // Then
        assertNotEquals(Boolean.TRUE, result, "Result should be false");
    }

    @Test
    void privateCanRead_invalidUserType_returnsFalse() {
        // When
        UserInfo userInfo = new UserInfo(new User("id", "displayName", "invalidUserType"));
        Boolean result = ReflectionTestUtils.invokeMethod(caseSecurityService, "canRead", userInfo, null, null);

        // Then
        assertNotEquals(Boolean.TRUE, result, "Result should be false");
    }

    @Test
    void canReadCaseInstance_ok_returnsValidResult() {
        // When
        boolean result = caseSecurityService.canRead(buildUserInfo("OFFICE", "o", null), buildCaseInstance("o", null));

        // Then
        assertTrue(result, "Result should be true");
    }

    @Test
    void canReadCaseInstance_nullInstance_returnsFalse() {
        // When
        boolean result = caseSecurityService.canRead(buildUserInfo("OFFICE", "o", null), (CaseInstance) null);

        // Then
        assertFalse(result, "Result should be false");
    }

    @Test
    void canEditCaseInstance_ok_returnsValidResult() {
        // When
        boolean result = caseSecurityService.canEdit(buildUserInfo("MO", null, "mo"), buildCaseInstance(null, "mo"));

        // Then
        assertTrue(result, "Result should be true");
    }

    @Test
    void canEditCaseInstance_nullInstance_returnsFalse() {
        // When
        boolean result = caseSecurityService.canEdit(buildUserInfo("OFFICE", "o", null), (CaseInstance) null);

        // Then
        assertFalse(result, "Result should be false");
    }

    @Test
    void canReadCaseInstance_noOperationVariable_returnsFalse() {
        // When
        boolean result = caseSecurityService.canEdit(buildUserInfo("OFFICE", "o", null), buildCaseInstanceWithoutOperation());

        // Then
        assertFalse(result, "Result should be false");
    }

    @Test
    void canReadCase_ok_returnsValidResult() {
        // When
        boolean result = caseSecurityService.canRead(buildUserInfo("OFFICE", "o", null), buildCase("o", null));

        // Then
        assertTrue(result, "Result should be true");
    }

    @Test
    void canReadCase_nullInstance_returnsFalse() {
        // When
        boolean result = caseSecurityService.canRead(buildUserInfo("OFFICE", "o", null), (Case) null);

        // Then
        assertFalse(result, "Result should be false");
    }

    @Test
    void canEditCase_ok_returnsValidResult() {
        // When
        boolean result = caseSecurityService.canEdit(buildUserInfo("MO", null, "mo"), buildCase(null, "mo"));

        // Then
        assertTrue(result, "Result should be true");
    }

    @Test
    void canEditCase_nullInstance_returnsFalse() {
        // When
        boolean result = caseSecurityService.canEdit(buildUserInfo("OFFICE", "o", null), (Case) null);

        // Then
        assertFalse(result, "Result should be false");
    }

    @Test
    void checkReadCaseInstance_cannotRead_throwsSecurityException() {
        // Given
        UserInfo userInfo = buildUserInfo("OFFICE", "o", null);
        CaseInstance caseInstance = buildCaseInstance("CLE-1", "CLE", "wrongOffice", null, true);

        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> caseSecurityService.checkRead(userInfo, caseInstance),
                "Should throw SecurityException");

        // Then
        assertEquals("User userId has no access to resource CLE-1 of type CLE", thrown.getMessage(), "Exception should contain a valid message");
    }

    @Test
    void checkEditCaseInstance_cannotEdit_throwsSecurityException() {
        // Given
        UserInfo userInfo = buildUserInfo("MO", null, "mo");
        CaseInstance caseInstance = buildCaseInstance("CLI-1", "CLI", null, "wrongMiddleOffice", true);

        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> caseSecurityService.checkEdit(userInfo, caseInstance),
                "Should throw SecurityException");

        // Then
        assertEquals("User userId has no access to resource CLI-1 of type CLI", thrown.getMessage(), "Exception should contain a valid message");
    }

    @Test
    void checkReadCase_cannotRead_throwsSecurityException() {
        // Given
        UserInfo userInfo = buildUserInfo("OFFICE", "o", null);
        Case caseInstance = buildCase("CLE-1", "CLE", "wrongOffice", null);
        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> caseSecurityService.checkRead(userInfo, caseInstance),
                "Should throw SecurityException");

        // Then
        assertEquals("User userId has no access to resource CLE-1 of type CLE", thrown.getMessage(), "Exception should contain a valid message");
    }

    @Test
    void checkEditCase_cannotEdit_throwsSecurityException() {
        // Given
        UserInfo userInfo = buildUserInfo("MO", null, "mo");
        Case caseInstance = buildCase("CLI-1", "CLI", null, "wrongMiddleOffice");

        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> caseSecurityService.checkEdit(userInfo, caseInstance),
                "Should throw SecurityException");

        // Then
        assertEquals("User userId has no access to resource CLI-1 of type CLI", thrown.getMessage(), "Exception should contain a valid message");
    }

    @Test
    void checkReadByCode_cannotEdit_throwsSecurityException() {
        skipTestIfElasticLess();
        // Given
        String code = "CLE-1";
        Case caseItem = buildCase(code, "CLE", "wrongOffice", null);
        when(caseUtils.getCaseByCode(code)).thenReturn(caseItem);
        UserInfo userInfo = buildUserInfo("OFFICE", "o", null);

        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> caseSecurityService.checkRead(userInfo, code),
                "Should throw SecurityException");

        // Then
        assertEquals("User userId has no access to resource CLE-1 of type CLE", thrown.getMessage(), "Exception should contain a valid message");
    }

    @Test
    void checkEditByCode_cannotEdit_throwsSecurityException() {
        skipTestIfElasticLess();
        // Given
        String code = "CLI-1";
        Case caseItem = buildCase(code, "CLI", null, "wrongMiddleOffice");
        when(caseUtils.getCaseByCode(code)).thenReturn(caseItem);
        UserInfo userInfo = buildUserInfo("MO", null, "mo");

        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> caseSecurityService.checkEdit(userInfo, code),
                "Should throw SecurityException");

        // Then
        assertEquals("User userId has no access to resource CLI-1 of type CLI", thrown.getMessage(), "Exception should contain a valid message");
    }

    @Test
    void privateThrowSecurityExceptionFromCase_noVariables_throwsValidSecurityException() {
        // Given
        UserInfo userInfo = new UserInfo(new User("id", "name", "BO"));
        Case caseInstance = new Case();
        caseInstance.setVariables(List.of());

        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> ReflectionTestUtils.invokeMethod(caseSecurityService, "throwSecurityException", userInfo, caseInstance),
                "Should throw SecurityException");

        // Then
        assertEquals("User id has no access to resource unknown of type unknown", thrown.getMessage(), "Exception thrown should have a valid message");
    }

    @Test
    void privateThrowSecurityExceptionFromCaseInstance_operationWithoutCode_throwsValidSecurityException() {
        // Given
        UserInfo userInfo = new UserInfo(new User("id", "name", "BO"));
        CaseInstance caseInstance = mock(CaseInstance.class);
        Map<String, Object> variables = new HashMap<>();
        DataObjectInstanceVariableContainerImpl operation = mock(DataObjectInstanceVariableContainerImpl.class);
        variables.put(OPERATION, operation);
        when(caseInstance.getCaseVariables()).thenReturn(variables);

        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> ReflectionTestUtils.invokeMethod(caseSecurityService, "throwSecurityException", userInfo, caseInstance),
                "Should throw SecurityException");

        // Then
        assertEquals("User id has no access to resource unknown of type unknown", thrown.getMessage(), "Exception thrown should have a valid message");
    }

    @Test
    void privateThrowSecurityExceptionFromCaseInstance_withVariables_throwsValidSecurityException() {
        // Given
        UserInfo userInfo = new UserInfo(new User("id", "name", "BO"));
        CaseInstance caseInstance = mock(CaseInstance.class);
        Map<String, Object> variables = new HashMap<>();
        DataObjectInstanceVariableContainerImpl operation = mock(DataObjectInstanceVariableContainerImpl.class);
        when(operation.getString(REQUEST_CODE)).thenReturn("code");
        variables.put(OPERATION, operation);
        variables.put(VARNAME_PRODUCT, "product");
        when(caseInstance.getCaseVariables()).thenReturn(variables);

        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> ReflectionTestUtils.invokeMethod(caseSecurityService, "throwSecurityException", userInfo, caseInstance),
                "Should throw SecurityException");

        // Then
        assertEquals("User id has no access to resource code of type product", thrown.getMessage(), "Exception thrown should have a valid message");
    }

    @Test
    void checkReadTaskByTaskId_cannotRead_throwsSecurityException() {
        // Given
        String taskId = "TSK-1";
        Map<String, Object> taskVariables = buildTaskVariables();
        when(platformTaskService.getTaskVariables(taskId)).thenReturn(taskVariables);
        UserInfo userInfo = buildUserInfo("OFFICE", "o", null);
        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> caseSecurityService.checkReadTask(userInfo, taskId),
                "Should throw SecurityException");
        // Then
        assertEquals("User userId has no access to resource code1 of type product1", thrown.getMessage(), "Exception should contain a valid message");
    }

    @Test
    void checkEditTaskByTaskId_cannotEdit_throwsSecurityException() {
        // Given
        String taskId = "TSK-1";
        Map<String, Object> taskVariables = buildTaskVariables();
        when(platformTaskService.getTaskVariables(taskId)).thenReturn(taskVariables);
        UserInfo userInfo = buildUserInfo("OFFICE", "o", null);
        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> caseSecurityService.checkEditTask(userInfo, taskId),
                "Should throw SecurityException");
        // Then
        assertEquals("User userId has no access to resource code1 of type product1", thrown.getMessage(), "Exception should contain a valid message");
    }

    @Test
    void checkReadTaskByTaskId_canRead_doNothing() {
        // Given
        String taskId = "TSK-1";
        Map<String, Object> taskVariables = buildTaskVariables();
        when(platformTaskService.getTaskVariables(taskId)).thenReturn(taskVariables);
        UserInfo userInfo = buildUserInfo("OFFICE", "office1", null);
        // When
        caseSecurityService.checkReadTask(userInfo, taskId);
    }


    @Test
    void checkEditTaskByTaskId_canEdit_doNothing() {
        // Given
        String taskId = "TSK-1";
        Map<String, Object> taskVariables = buildTaskVariables();
        when(platformTaskService.getTaskVariables(taskId)).thenReturn(taskVariables);
        UserInfo userInfo = buildUserInfo("OFFICE", "office1", null);
        // When
        caseSecurityService.checkEditTask(userInfo, taskId);
    }

    @Test
    void checkReadTaskByTaskVariables_cannotRead_throwsSecurityException() {
        // Given
        Map<String, Object> taskVariables = buildTaskVariables();
        UserInfo userInfo = buildUserInfo("OFFICE", "o", null);
        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> caseSecurityService.checkReadTask(userInfo, taskVariables),
                "Should throw SecurityException");
        // Then
        assertEquals("User userId has no access to resource code1 of type product1", thrown.getMessage(), "Exception should contain a valid message");
    }

    @Test
    void checkReadTaskByTaskVariables_cannotEdit_throwsSecurityException() {
        // Given
        Map<String, Object> taskVariables = buildTaskVariables();
        UserInfo userInfo = buildUserInfo("OFFICE", "o", null);
        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> caseSecurityService.checkEditTask(userInfo, taskVariables),
                "Should throw SecurityException");
        // Then
        assertEquals("User userId has no access to resource code1 of type product1", thrown.getMessage(), "Exception should contain a valid message");
    }

    @Test
    void checkReadTaskByTaskVariables_nullTaskVariables_throwsResourceNotFoundException() {
        // Given
        UserInfo userInfo = buildUserInfo("OFFICE", "o", null);
        // When
        ServiceException exception = assertThrows(ResourceNotFoundException.class,
                () -> caseSecurityService.checkReadTask(userInfo, (Map<String, Object>) null),
                "Should throw SecurityException");
        // Then
        assertEquals("No task data found", exception.getMessage());
        assertEquals("containsDataTask", exception.getKey());
    }

    @Test
    void checkReadTaskByTaskVariables_nullRoot_throwsSecurityException() {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        UserInfo userInfo = buildUserInfo("OFFICE", "o", null);
        // When
        ServiceException exception = assertThrows(ResourceNotFoundException.class,
                () -> caseSecurityService.checkReadTask(userInfo, taskVariables),
                "Should throw SecurityException");
        // Then
        assertEquals("No task data found", exception.getMessage());
        assertEquals("containsDataTask", exception.getKey());
    }

    @Test
    void checkReadTaskByTaskVariables_nullOperation_throwsSecurityException() {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        Map<String, Object> root = new HashMap<>();
        root.put("product", "product1");
        taskVariables.put("root", root);
        UserInfo userInfo = buildUserInfo("OFFICE", "o", null);
        // When
        ServiceException exception = assertThrows(ResourceNotFoundException.class,
                () -> caseSecurityService.checkReadTask(userInfo, taskVariables),
                "Should throw SecurityException");
        // Then
        assertEquals("No task data found", exception.getMessage());
        assertEquals("containsDataTask", exception.getKey());
    }

    @Test
    void checkReadByCaseVariables_cannotRead_throwsSecurityException() {
        // Given
        Map<String, Object> caseVariables = buildCaseVariables();
        UserInfo userInfo = buildUserInfo("OFFICE", "o", null);
        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> caseSecurityService.checkRead(userInfo, caseVariables),
                "Should throw SecurityException");
        // Then
        assertEquals("User userId has no access to resource code1 of type product", thrown.getMessage(), "Exception should contain a valid message");
    }

    @Test
    void checkReadByCaseVariables_nullOperation_throwsSecurityException() {
        // Given
        Map<String, Object> caseVariables = new HashMap<>();
        Map<String, Object> root = new HashMap<>();
        root.put("product", "product1");
        caseVariables.put("root", root);
        UserInfo userInfo = buildUserInfo("OFFICE", "o", null);
        // When
        ServiceException exception = assertThrows(ResourceNotFoundException.class,
                () -> caseSecurityService.checkRead(userInfo, caseVariables),
                "Should throw SecurityException");
        // Then
        assertEquals("No case data found", exception.getMessage());
        assertEquals("containsData", exception.getKey());
    }

    private static Stream<Arguments> privateCanReadAttributes() {
        return Stream.of(
                Arguments.of(buildUserInfo("OFFICE", "caseOffice1", null), "caseOffice1", null, true),
                Arguments.of(buildUserInfo("OFFICE", "caseOffice2", null), "wrongOffice2", null, false),
                Arguments.of(buildUserInfo("OFFICE", null, null), "caseOffice3", null, false),
                Arguments.of(buildUserInfo("OFFICE", "caseOffice4", null), null, null, false),
                Arguments.of(buildUserInfo("BO", null, null), null, null, true),
                Arguments.of(buildUserInfo("CUSTOMER", null, null), null, null, true),
                Arguments.of(buildUserInfo("MO", null, "caseMiddleOffice5"), null, "caseMiddleOffice5", true),
                Arguments.of(buildUserInfo("MO", null, "caseMiddleOffice6"), null, "wrongMiddleOffice6", false),
                Arguments.of(buildUserInfo("MO", null, null), null, "caseMiddleOffice7", false),
                Arguments.of(buildUserInfo("MO", null, "caseMiddleOffice8"), null, null, false)
        );
    }

    private static UserInfo buildUserInfo(String type, String office, String middleOffice) {
        UserInfo result = new UserInfo(new User("userId", "userDisplayedName", type));
        result.setOffice(office);
        result.setMiddleOffice(middleOffice);
        return result;
    }

    private CaseInstance buildCaseInstance(String office, String middleOffice) {
        return buildCaseInstance(null, null, office, middleOffice, false);
    }

    private CaseInstance buildCaseInstance(String code, String product, String office, String middleOffice, boolean willFail) {
        CaseInstance result = mock(CaseInstance.class);
        Map<String, Object> variables = new HashMap<>();
        DataObjectInstanceVariableContainerImpl operation = mock(DataObjectInstanceVariableContainerImpl.class);
        when(operation.getString(REQUEST_OFFICE)).thenReturn(office);
        when(operation.getString(REQUEST_MIDDLE_OFFICE)).thenReturn(middleOffice);
        variables.put(OPERATION, operation);
        variables.put(VARNAME_PRODUCT, product);
        if (willFail) {
            when(operation.getString(REQUEST_CODE)).thenReturn(code);
        }
        when(result.getCaseVariables()).thenReturn(variables);
        return result;
    }

    private CaseInstance buildCaseInstanceWithoutOperation() {
        CaseInstance result = mock(CaseInstance.class);
        Map<String, Object> variables = new HashMap<>();
        variables.put(VARNAME_PRODUCT, "product");
        when(result.getCaseVariables()).thenReturn(variables);
        return result;
    }

    private Case buildCase(String office, String middleOffice) {
        return buildCase(null, null, office, middleOffice);
    }

    private Case buildCase(String code, String product, String office, String middleOffice) {
        Case result = new Case();
        Variable codeVariable = buildStringCaseVariable(OPERATION_CODE, code);
        Variable productVariable = buildStringCaseVariable(PRODUCT, product);
        Variable officeVariable = buildStringCaseVariable(OPERATION_OFFICE, office);
        Variable middleOfficeVariable = buildStringCaseVariable(OPERATION_MIDDLE_OFFICE, middleOffice);
        result.setVariables(List.of(codeVariable, productVariable, officeVariable, middleOfficeVariable));
        return result;
    }

    private Variable buildStringCaseVariable(String name, String value) {
        Variable result = new Variable();
        result.setName(name);
        result.setType("string");
        result.setTextValue(value);
        return result;
    }

    private Map<String, Object> buildTaskVariables() {
        Map<String, Object> taskVariables = new HashMap<>();
        Map<String, Object> root = new HashMap<>();
        Map<String, Object> operation = new HashMap<>();
        operation.put("code", "code1");
        operation.put("office", "office1");
        operation.put("middleOffice", "middleOffice1");
        root.put("operation", operation);
        root.put("product", "product1");
        taskVariables.put("root", root);
        return taskVariables;
    }

    private Map<String, Object> buildCaseVariables() {
        Map<String, Object> caseVariables = new HashMap<>();
        Map<String, Object> operation = new HashMap<>();
        operation.put("code", "code1");
        operation.put("product", "product");
        operation.put("office", "office1");
        operation.put("middleOffice", "middleOffice1");
        ServiceRegistryDataObjectModel serviceRegistryDataObjectModel = mock(ServiceRegistryDataObjectModel.class);
        DataObjectInstanceVariableContainerImpl container = new DataObjectInstanceVariableContainerImpl(operation, null, null, null, "key", serviceRegistryDataObjectModel);
        caseVariables.put("operation", container);
        return caseVariables;
    }
}
