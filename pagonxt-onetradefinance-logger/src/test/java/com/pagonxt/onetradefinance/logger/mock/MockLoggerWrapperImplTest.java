package com.pagonxt.onetradefinance.logger.mock;


import com.pagonxt.onetradefinance.logger.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class MockLoggerWrapperImplTest {

    @InjectMocks
    MockLoggerWrapperImpl mockLoggerWrapper;

    @Mock
    Logger logger;

    @Captor
    ArgumentCaptor<String> messageCaptor;

    @ParameterizedTest
    @MethodSource("provideArguments")
    void businessLog_whenObjects_thenLogStrings(String expectedMessage, Object object1, Object object2) throws Exception {
        // When
        setLoggerMock();
        when(logger.isInfoEnabled()).thenReturn(true);
        // Given
        mockLoggerWrapper.businessLog("info", object1, object2);
        // Then
        verify(logger).info(messageCaptor.capture());
        assertEquals(expectedMessage, messageCaptor.getValue());
    }

    @Test
    void businessLog_whenNoObjects_thenDoNotLog() throws Exception {
        // When
        setLoggerMock();
        // Given
        mockLoggerWrapper.businessLog("info");
        // Then
        verify(logger, never()).isInfoEnabled();
        verify(logger, never()).info(any());
    }

    @Test
    void businessLog_whenLevelTrace_thenLogTrace() throws Exception {
        // When
        setLoggerMock();
        when(logger.isTraceEnabled()).thenReturn(true);
        String expectedMessage = "test1 - test2";
        // Given
        mockLoggerWrapper.businessLog("trace", "test1", "test2");
        // Then
        verify(logger).trace(messageCaptor.capture());
        assertEquals(expectedMessage, messageCaptor.getValue());
    }

    @Test
    void businessLog_whenLevelError_thenLogTrace() throws Exception {
        // When
        setLoggerMock();
        when(logger.isErrorEnabled()).thenReturn(true);
        String expectedMessage = "test1 - test2";
        // Given
        mockLoggerWrapper.businessLog("error", "test1", "test2");
        // Then
        verify(logger).error(messageCaptor.capture());
        assertEquals(expectedMessage, messageCaptor.getValue());
    }

    @Test
    void businessLog_whenLevelInfo_thenLogInfo() throws Exception {
        // When
        setLoggerMock();
        when(logger.isInfoEnabled()).thenReturn(true);
        String expectedMessage = "test1 - test2";
        // Given
        mockLoggerWrapper.businessLog("info", "test1", "test2");
        // Then
        verify(logger).info(messageCaptor.capture());
        assertEquals(expectedMessage, messageCaptor.getValue());
    }


    @Test
    void businessLog_whenLevelDebug_thenLogDebug() throws Exception {
        // When
        setLoggerMock();
        when(logger.isDebugEnabled()).thenReturn(true);
        String expectedMessage = "test1 - test2";
        // Given
        mockLoggerWrapper.businessLog("debug", "test1", "test2");
        // Then
        verify(logger).debug(messageCaptor.capture());
        assertEquals(expectedMessage, messageCaptor.getValue());
    }

    @Test
    void businessLog_whenLevelOther_thenLogDebug() throws Exception {
        // When
        setLoggerMock();
        when(logger.isDebugEnabled()).thenReturn(true);
        String expectedMessage = "test1 - test2";
        // Given
        mockLoggerWrapper.businessLog("test", "test1", "test2");
        // Then
        verify(logger).debug(messageCaptor.capture());
        assertEquals(expectedMessage, messageCaptor.getValue());
    }

    private static Stream<Arguments> provideArguments() {
        return Stream.of(
                Arguments.of("test1 - test2", "test1", "test2"),
                Arguments.of("1 - 2.4", 1, 2.4),
                Arguments.of("null - null", null, null)
        );
    }

    void setLoggerMock() throws Exception {
        Field field = MockLoggerWrapperImpl.class.getDeclaredField("log");
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, logger);
    }

}
