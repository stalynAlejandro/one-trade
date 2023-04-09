package com.pagonxt.onetradefinance.external.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.service.DirectoryService;
import com.pagonxt.onetradefinance.integrations.service.OfficeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@UnitTest
class UserInfoServiceBasicImplTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    UserInfoServiceBasicImpl userInfoServiceBasicImpl;

    @Mock
    UserService userService;
    @Mock
    DirectoryService directoryService;
    @Mock
    OfficeService officeService;

    @Test
    void getUserInfo_whenUserFound_thenReturnUserInfo() throws IOException {
        // Given
        UserInfo expectedUserInfo = mapper.readValue(new ClassPathResource("data/model/user-info.json").getFile(), UserInfo.class);
        User user = new User("user1", "User Name", "OFFICE");
        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.getCurrentUserCountry()).thenReturn("ES");
        when(officeService.getRequesterOffice(user)).thenReturn("1234");
        when(officeService.getRequesterMiddleOffice(user)).thenReturn(null);
        when(directoryService.getUserEmail(user)).thenReturn("user1@mail.com");
        // When
        UserInfo result = userInfoServiceBasicImpl.getUserInfo();
        // Then
        assertEquals(expectedUserInfo, result);
    }

    @Test
    void getUserInfo_whenUserNotFound_thenReturnServiceException() {
        // Given
        when(userService.getCurrentUser()).thenReturn(null);
        // When
        ServiceException exception = assertThrows(ServiceException.class, () -> userInfoServiceBasicImpl.getUserInfo());
        // Then
        assertEquals("getUserInfoError", exception.getKey());
        assertEquals("Error retrieving the current user", exception.getMessage());
    }

    @Test
    void getUserInfoWithLocale_whenUserFound_thenReturnUserInfo() throws IOException {
        // Given
        UserInfo expectedUserInfo = mapper.readValue(new ClassPathResource("data/model/user-info.json").getFile(), UserInfo.class);
        expectedUserInfo.setLocale("es_es");
        User user = new User("user1", "User Name", "OFFICE");
        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.getCurrentUserCountry()).thenReturn("ES");
        when(officeService.getRequesterOffice(user)).thenReturn("1234");
        when(officeService.getRequesterMiddleOffice(user)).thenReturn(null);
        when(directoryService.getUserEmail(user)).thenReturn("user1@mail.com");
        // When
        UserInfo result = userInfoServiceBasicImpl.getUserInfo("es_es");
        // Then
        assertEquals(expectedUserInfo, result);
    }

}
