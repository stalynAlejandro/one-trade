package com.pagonxt.onetradefinance.work.security;

import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.SecurityException;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.model.Case;
import com.pagonxt.onetradefinance.work.utils.CaseUtils;
import org.flowable.content.api.ContentItem;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@UnitTest
class DocumentSecurityServiceImplTest {

    @Mock
    CaseSecurityService caseSecurityService;

    @Mock
    CaseUtils caseUtils;

    @InjectMocks
    DocumentSecurityServiceImpl documentSecurityService;

    @Test
    void checkReadDocument_ok_invokesCaseSecurityService() {
        // Given
        UserInfo userInfo = mock(UserInfo.class);
        ContentItem contentItem = mock(ContentItem.class);
        when(caseUtils.getDocumentCaseId(contentItem)).thenReturn("caseId");
        Case caseItem = mock(Case.class);
        when(caseUtils.getCaseById("caseId")).thenReturn(caseItem);

        // When
        documentSecurityService.checkReadDocument(userInfo, contentItem);

        // Then
        verify(caseSecurityService).checkRead(userInfo, caseItem);
    }

    @Test
    void checkReadDocument_unauthorized_throwsSecurityException() {
        // Given
        User user = mock(User.class);
        when(user.getUserId()).thenReturn("user");
        UserInfo userInfo = new UserInfo(user);
        ContentItem contentItem = mock(ContentItem.class);
        when(contentItem.getId()).thenReturn("documentId");
        when(caseUtils.getDocumentCaseId(contentItem)).thenReturn("caseId");
        Case caseItem = mock(Case.class);
        when(caseUtils.getCaseById("caseId")).thenReturn(caseItem);
        doThrow(new SecurityException("user", "caseId", "CLE")).when(caseSecurityService).checkRead(userInfo, caseItem);

        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> documentSecurityService.checkReadDocument(userInfo, contentItem),
                "Should throw SecurityException");

        // Then
        assertEquals("User user has no access to resource documentId of type document", thrown.getMessage(), "Thrown exception should have a laid message");
    }

    @Test
    void checkEditDocument_ok_invokesCaseSecurityService() {
        // Given
        UserInfo userInfo = mock(UserInfo.class);
        ContentItem contentItem = mock(ContentItem.class);
        when(caseUtils.getDocumentCaseId(contentItem)).thenReturn("caseId");
        Case caseItem = mock(Case.class);
        when(caseUtils.getCaseById("caseId")).thenReturn(caseItem);

        // When
        documentSecurityService.checkEditDocument(userInfo, contentItem);

        // Then
        verify(caseSecurityService).checkEdit(userInfo, caseItem);
    }

    @Test
    void checkEditDocument_unauthorized_throwsSecurityException() {
        // Given
        User user = mock(User.class);
        when(user.getUserId()).thenReturn("user");
        UserInfo userInfo = new UserInfo(user);
        ContentItem contentItem = mock(ContentItem.class);
        when(contentItem.getId()).thenReturn("documentId");
        when(caseUtils.getDocumentCaseId(contentItem)).thenReturn("caseId");
        Case caseItem = mock(Case.class);
        when(caseUtils.getCaseById("caseId")).thenReturn(caseItem);
        doThrow(new SecurityException("user", "caseId", "CLE")).when(caseSecurityService).checkEdit(userInfo, caseItem);

        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> documentSecurityService.checkEditDocument(userInfo, contentItem),
                "Should throw SecurityException");

        // Then
        assertEquals("User user has no access to resource documentId of type document", thrown.getMessage(), "Thrown exception should have a laid message");
    }
}
