package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UnitTest
class MockedOfficeServiceTest {

    @InjectMocks
    MockedOfficeService mockedOfficeService = new MockedOfficeService();

    @ParameterizedTest
    @MethodSource("provideValidOfficeUsers")
    void getRequesterOffice_whenMockedUser_returnOffice(String userId, String office) {
        // Given
        User user = new User(userId, null, "OFFICE");
        // When
        String result = mockedOfficeService.getRequesterOffice(user);
        // Then
        assertEquals(office, result);
    }

    private static Stream<Arguments> provideValidOfficeUsers() {
        return Stream.of(
                Arguments.of("olivia", "1234"),
                Arguments.of("omar", "1237"),
                Arguments.of("oscar", "1236"),
                Arguments.of("other", "1234")
        );
    }

    @Test
    void getRequesterOffice_whenInvalidTypeUser_returnNull() {
        // Given
        User user = new User("other", null, "MO");
        // When
        String result = mockedOfficeService.getRequesterOffice(user);
        // Then
        assertNull(result);
    }

    @ParameterizedTest
    @MethodSource("provideValidMiddleOfficeUsers")
    void getRequesterMiddleOffice_whenMockedUser_returnOffice(String userId, String middleOffice) {
        // Given
        User user = new User(userId, null, "MO");
        // When
        String result = mockedOfficeService.getRequesterMiddleOffice(user);
        // Then
        assertEquals(middleOffice, result);
    }

    private static Stream<Arguments> provideValidMiddleOfficeUsers() {
        return Stream.of(
                Arguments.of("marta", "8911"),
                Arguments.of("manuel", "8908"),
                Arguments.of("other", "8911")
        );
    }

    @Test
    void getRequesterMiddleOffice_whenInvalidTypeUser_returnNull() {
        // Given
        User user = new User("other", null, "OFFICE");
        // When
        String result = mockedOfficeService.getRequesterMiddleOffice(user);
        // Then
        assertNull(result);
    }

    private static Stream<Arguments> provideValidOffices() {
        return Stream.of(
                Arguments.of("1234", "middleOffice1234"),
                Arguments.of("1235", "middleOffice1235"),
                Arguments.of("1236", "middleOffice1235"),
                Arguments.of("other", "middleOffice1234")
        );
    }

    private static Stream<Arguments> provideValidMiddleOffices() {
        return Stream.of(
                Arguments.of("middleOffice1234","1234",  0),
                Arguments.of("middleOffice1235","1235",  0),
                Arguments.of("middleOffice1235","1236",  1),
                Arguments.of("other", "1234",  0)
        );
    }

}
