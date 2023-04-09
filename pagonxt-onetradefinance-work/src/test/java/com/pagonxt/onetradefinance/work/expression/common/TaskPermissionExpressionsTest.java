package com.pagonxt.onetradefinance.work.expression.common;

import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.utils.CaseUtils;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.identitylink.api.IdentityLinkType;
import org.flowable.identitylink.service.impl.persistence.entity.IdentityLinkEntityImpl;
import org.flowable.idm.api.GroupQuery;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class TaskPermissionExpressionsTest {

    @Mock
    TaskService taskService;

    @Mock
    CmmnRuntimeService cmmnRuntimeService;

    @Mock
    IdmIdentityService idmIdentityService;

    @Mock
    CaseUtils caseUtils;

    @InjectMocks
    TaskPermissionExpressions taskPermissionExpressions;

    @Test
    void resolvePermissions_createsVariableInTask() {
        // Given
        String caseId = "caseId";
        doReturn(caseId).when(caseUtils).getCaseIdFromDescendantExecution(any());

        doReturn("CLE").when(cmmnRuntimeService).getVariable(caseId, PRODUCT);
        doReturn("REQUEST").when(cmmnRuntimeService).getVariable(caseId, EVENT);
        doReturn("ESP").when(cmmnRuntimeService).getVariable(caseId, COUNTRY);
        GroupQuery groupQuery = mock(GroupQuery.class);
        when(idmIdentityService.createGroupQuery()).thenReturn(groupQuery);
        when(groupQuery.groupId("BOI_User")).thenReturn(groupQuery);
        when(groupQuery.singleResult()).thenReturn(null);

        DelegateExecution delegateExecution = mock(DelegateExecution.class);
        TaskEntity taskEntity = mock(TaskEntity.class);
        IdentityLinkEntityImpl identityLinkEntityImpl = mock(IdentityLinkEntityImpl.class);
        doReturn("candidate").when(identityLinkEntityImpl).getType();
        doReturn("BOI_User").when(identityLinkEntityImpl).getGroupId();
        List<IdentityLinkEntityImpl> identityList = new ArrayList<>();
        identityList.add(identityLinkEntityImpl);
        doReturn(identityList).when(taskEntity).getIdentityLinks();
        doReturn("taskId").when(taskEntity).getId();

        // When
        taskPermissionExpressions.resolvePermissions(delegateExecution, taskEntity);

        // Then
        String candidateGroup = "grpRes_ESP_CLE_REQUEST_BOI_User";
        verify(taskService).addGroupIdentityLink(taskEntity.getId(), candidateGroup, IdentityLinkType.CANDIDATE);
    }

    @Test
    void resolvePermissions_whenNoVariablesAvailable_thenThrowsNoSuchElementException() {
        // Given
        doReturn(null).when(caseUtils).getCaseIdFromDescendantExecution(any());
        GroupQuery groupQuery = mock(GroupQuery.class);

        DelegateExecution delegateExecution = mock(DelegateExecution.class);
        TaskEntity taskEntity = mock(TaskEntity.class);
        IdentityLinkEntityImpl identityLinkEntityImpl = mock(IdentityLinkEntityImpl.class);
        doReturn("candidate").when(identityLinkEntityImpl).getType();
        doReturn("BOI_User").when(identityLinkEntityImpl).getGroupId();
        List<IdentityLinkEntityImpl> identityList = new ArrayList<>();
        identityList.add(identityLinkEntityImpl);
        doReturn(identityList).when(taskEntity).getIdentityLinks();
        doReturn("taskId").when(taskEntity).getId();

        // When
        Exception exception = assertThrows(NoSuchElementException.class,
                () -> taskPermissionExpressions.resolvePermissions(delegateExecution, taskEntity),
                "Should throw NoSuchElementException if product, event or task properties are not found");

        // Then
        assertEquals("Error recovering some mandatory case variable:  product, event, country", exception.getMessage(),
                "Exception message should match pattern");
    }

    @Test
    void resolvePermissions_replaceCandidateGroup() {
        // Given
        String caseId = "caseId";
        doReturn(caseId).when(caseUtils).getCaseIdFromDescendantExecution(any());

        doReturn("CLE").when(cmmnRuntimeService).getVariable(caseId, PRODUCT);
        doReturn("REQUEST").when(cmmnRuntimeService).getVariable(caseId, EVENT);
        doReturn("ESP").when(cmmnRuntimeService).getVariable(caseId, COUNTRY);
        GroupQuery groupQuery = mock(GroupQuery.class);
        when(idmIdentityService.createGroupQuery()).thenReturn(groupQuery);
        when(groupQuery.groupId("BOI_User")).thenReturn(groupQuery);
        when(groupQuery.singleResult()).thenReturn(null);

        DelegateExecution delegateExecution = mock(DelegateExecution.class);
        TaskEntity taskEntity = mock(TaskEntity.class);
        IdentityLinkEntityImpl identityLinkEntityImpl = mock(IdentityLinkEntityImpl.class);
        doReturn("candidate").when(identityLinkEntityImpl).getType();
        doReturn("BOI_User").when(identityLinkEntityImpl).getGroupId();
        List<IdentityLinkEntityImpl> identityList = new ArrayList<>();
        identityList.add(identityLinkEntityImpl);
        doReturn(identityList).when(taskEntity).getIdentityLinks();
        doReturn("taskId").when(taskEntity).getId();

        // When
        taskPermissionExpressions.resolvePermissions(delegateExecution, taskEntity);

        // Then
        String candidateGroup = "grpRes_ESP_CLE_REQUEST_BOI_User";
        verify(taskService).addGroupIdentityLink(taskEntity.getId(), candidateGroup, IdentityLinkType.CANDIDATE);
    }
}
