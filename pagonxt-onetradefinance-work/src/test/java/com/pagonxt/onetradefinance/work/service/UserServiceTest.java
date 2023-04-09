package com.pagonxt.onetradefinance.work.service;

import com.flowable.core.idm.api.PlatformIdentityInfo;
import com.flowable.core.idm.api.PlatformIdentityService;
import com.flowable.core.idm.api.PlatformUser;
import com.flowable.core.idm.api.PlatformUserQuery;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.security.UserSecurityService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class UserServiceTest {

    @Mock
    UserSecurityService userSecurityService;

    @Mock
    PlatformIdentityService platformIdentityService;

    @InjectMocks
    UserService userService;

    @Test
    void getUsersByUserProfile_isOk_returnsList() {
        // Given
        String userProfile = "userProfile";

        PlatformUser platformUser = mock(PlatformUser.class);
        PlatformIdentityInfo platformIdentityInfo = mock(PlatformIdentityInfo.class);
        List<PlatformIdentityInfo> platformIdentityInfoList = new ArrayList<>();
        platformIdentityInfoList.add(platformIdentityInfo);
        doReturn(userProfile).when(platformIdentityInfo).getName();
        doReturn(userProfile).when(platformIdentityInfo).getValue();
        doReturn(platformIdentityInfoList).when(platformUser).getIdentityInfo();
        List<PlatformUser> possibleUsers = new ArrayList<>();
        possibleUsers.add(platformUser);

        PlatformUserQuery platformUserQuery = mock(PlatformUserQuery.class);
        doReturn(possibleUsers).when(platformUserQuery).list();

        // When
        List<PlatformUser> result = this.userService.getUsersByUserProfile(userProfile, platformUserQuery);

        // Then
        assertThat(result).isEqualTo(possibleUsers);
    }

    @Test
    void getAvailableUsers_isOk_returnsList() {
        // Given
        String groups = "grpRes_ESP_XXX_User";
        PlatformUserQuery platformUserQueryPos = mock(PlatformUserQuery.class);
        List<PlatformUser> possibleList = new ArrayList<>();
        PlatformUser platformUser = mock(PlatformUser.class);
        doReturn("ACTIVE").when(platformUser).getState();
        possibleList.add(platformUser);
        doReturn(platformUserQueryPos).when(platformIdentityService).createPlatformUserQuery();
        doReturn(platformUserQueryPos).when(platformUserQueryPos).memberOfGroup(any());
        doReturn(possibleList).when(platformUserQueryPos).list();

        // When
        List<PlatformUser> result = this.userService.getAvailableUsers(groups);

        // Then
        verify(platformIdentityService).createPlatformUserQuery();
        assertThat(result).isEqualTo(possibleList);
    }
}
