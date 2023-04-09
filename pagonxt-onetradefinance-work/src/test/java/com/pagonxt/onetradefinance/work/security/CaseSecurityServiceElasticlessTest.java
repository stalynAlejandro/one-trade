package com.pagonxt.onetradefinance.work.security;

import com.flowable.platform.service.task.PlatformTaskService;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.work.config.TestUtils;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.utils.CaseUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@UnitTest
class CaseSecurityServiceElasticlessTest {

    @Mock
    static CaseUtils caseUtils;
    @Mock
    static PlatformTaskService platformTaskService;

    static CaseSecurityService caseSecurityService;

    private void skipTestIfElasticLess() {
        TestUtils.skipTestIfSpringProfileActive("elasticless");
    }

    @BeforeAll
    static void setup() {
        caseSecurityService = new CaseSecurityServiceElasticless(caseUtils, platformTaskService);
    }

    @Test
    void checkReadByCode_doesNotThrowSecurityException() {
        skipTestIfElasticLess();
        // Given
        String code = "CLE-1";
        UserInfo userInfo = buildUserInfo("OFFICE", "o", null);

        // When and then
        assertDoesNotThrow(() -> caseSecurityService.checkRead(userInfo, code),
                "Should not throw SecurityException");
    }

    @Test
    void checkEditByCode_doesNotThrowSecurityException() {
        skipTestIfElasticLess();
        // Given
        String code = "CLI-1";
        UserInfo userInfo = buildUserInfo("MO", null, "mo");

        // When and then
        assertDoesNotThrow(
                () -> caseSecurityService.checkEdit(userInfo, code),
                "Should not throw SecurityException");
    }

    private static UserInfo buildUserInfo(String type, String office, String middleOffice) {
        UserInfo result = new UserInfo(new User("userId", "userDisplayedName", type));
        result.setOffice(office);
        result.setMiddleOffice(middleOffice);
        return result;
    }
}
