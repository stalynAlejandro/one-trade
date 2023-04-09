package com.pagonxt.onetradefinance.integrations.apis.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.exception.ExpiredTokenException;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
class TokenErrorHandlerTest {

    @InjectMocks
    TokenErrorHandler tokenErrorHandler;
    @Spy
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void handleError_ok_thenThrowIntegrationException() {
        // Given
        ClientHttpResponse response = new MockClientHttpResponse(("{\n\"errors\":[\n{\n\"code\":\"code\",\n\"message\":\"message\",\n" +
                "\"level\":\"level\",\n\"description\":\"description\"\n}\n]\n}")
                .getBytes(StandardCharsets.UTF_8), HttpStatus.INTERNAL_SERVER_ERROR);
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        ReflectionTestUtils.setField(response, "headers", headers);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        // When
        ServiceException exception = assertThrows(IntegrationException.class, () -> tokenErrorHandler.handleError(response, httpStatus));
        // Then
        assertEquals("errorIntegration", exception.getKey());
        assertEquals("Error connecting with external API", exception.getMessage());
    }

    @Test
    void handleError_whenExpiredCredential_thenThrowExpiredTokenException() {
        // Given
        ClientHttpResponse response = new MockClientHttpResponse(("{\n\"errors\":[\n{\n\"code\":\"code\",\n\"message\":\"expired_credential\",\n" +
                "\"level\":\"level\",\n\"description\":\"description\"\n}\n]\n}")
                .getBytes(StandardCharsets.UTF_8), HttpStatus.INTERNAL_SERVER_ERROR);
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        ReflectionTestUtils.setField(response, "headers", headers);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        // When
        ServiceException exception = assertThrows(ExpiredTokenException.class, () -> tokenErrorHandler.handleError(response, httpStatus));
        // Then
        assertEquals("errorToken", exception.getKey());
        assertEquals("The token has expired", exception.getMessage());
    }

    @Test
    void handleError_whenTokenNotActive_thenThrowExpiredTokenException() {
        // Given
        ClientHttpResponse response = new MockClientHttpResponse(("{\n\"errors\":[\n{\n\"code\":\"TOKEN_NOT_ACTIVE\",\n\"message\":\"message\",\n" +
                "\"level\":\"level\",\n\"description\":\"description\"\n}\n]\n}")
                .getBytes(StandardCharsets.UTF_8), HttpStatus.INTERNAL_SERVER_ERROR);
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        ReflectionTestUtils.setField(response, "headers", headers);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        // When
        ServiceException exception = assertThrows(ExpiredTokenException.class, () -> tokenErrorHandler.handleError(response, httpStatus));
        // Then
        assertEquals("errorToken", exception.getKey());
        assertEquals("The token has expired", exception.getMessage());
    }

    @Test
    void handleError_whenMoreThanOneError_thenThrowIntegrationException() {
        // Given
        ClientHttpResponse response = new MockClientHttpResponse(("{\n\"errors\": [\n{\n\"code\": \"code1\",\n" +
                "\"message\": \"expired_credential\",\n\"level\": \"level1\",\n\"description\": \"description1\"\n},\n" +
                "{\n\"code\": \"code2\",\n\"message\": \"message2\",\n\"level\": \"level2\",\n" +
                "\"description\": \"description2\"\n}\n]\n}")
                .getBytes(StandardCharsets.UTF_8), HttpStatus.INTERNAL_SERVER_ERROR);
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        ReflectionTestUtils.setField(response, "headers", headers);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        // When
        ServiceException exception = assertThrows(IntegrationException.class, () -> tokenErrorHandler.handleError(response, httpStatus));
        // Then
        assertEquals("errorIntegration", exception.getKey());
        assertEquals("Error connecting with external API", exception.getMessage());
    }

    @Test
    void handleError_whenIOException_thenHandleError() {
        // Given
        ClientHttpResponse response = new MockClientHttpResponse(("badFormat")
                .getBytes(StandardCharsets.UTF_8), HttpStatus.INTERNAL_SERVER_ERROR);
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        ReflectionTestUtils.setField(response, "headers", headers);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        // When and Then
        assertThrows(HttpClientErrorException.BadRequest.class, () -> tokenErrorHandler.handleError(response, httpStatus));
    }
}