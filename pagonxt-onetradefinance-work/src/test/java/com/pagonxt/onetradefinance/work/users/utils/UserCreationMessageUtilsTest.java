package com.pagonxt.onetradefinance.work.users.utils;

import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.users.bot.Response;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class UserCreationMessageUtilsTest {

    @InjectMocks
    UserCreationMessageUtils userCreationMessageUtils;

    @Test
    void allLanguagesLoginMsgError_messageIsOk() {
        // Given
        String id = "test";

        // When
        Response result = userCreationMessageUtils.allLanguagesLoginMsgError(id);

        // Then
        Response expectedResult = new Response();
        expectedResult.setResponse("messageEN", String.format("A user with id '%s' already exists", id));
        expectedResult.setResponse("messageESP", String.format("Un usuario con id '%s' ya existe", id));
        expectedResult.setSuccess(false);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void userCreated_messageIsOk() {
        // When
        Response result = userCreationMessageUtils.userCreated();

        // Then
        Response expectedResult = new Response();
        expectedResult.setResponse("messageEN", "User created successfully");
        expectedResult.setResponse("messageESP", "Usuario creado con éxito");
        expectedResult.setSuccess(true);

        assertThat(result).isEqualTo(expectedResult);
    }
}