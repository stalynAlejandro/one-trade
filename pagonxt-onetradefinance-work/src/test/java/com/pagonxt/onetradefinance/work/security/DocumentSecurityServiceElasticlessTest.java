package com.pagonxt.onetradefinance.work.security;

import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.flowable.content.api.ContentItem;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

@UnitTest
class DocumentSecurityServiceElasticlessTest {

    @InjectMocks
    DocumentSecurityServiceElasticless documentSecurityService;

    @Test
    void checkReadDocument_doesNotThrowSecurityException() {
        // Given
        UserInfo userInfo = new UserInfo(mock(User.class));
        ContentItem contentItem = mock(ContentItem.class);

        // When and then
        assertDoesNotThrow(
                () -> documentSecurityService.checkReadDocument(userInfo, contentItem),
                "Should not throw SecurityException");
    }

    @Test
    void checkEditDocument_doesNotThrowSecurityException() {
        // Given
        UserInfo userInfo = new UserInfo(mock(User.class));
        ContentItem contentItem = mock(ContentItem.class);

        // When and then
        assertDoesNotThrow(
                () -> documentSecurityService.checkEditDocument(userInfo, contentItem),
                "Should not throw SecurityException");
    }
}
