package com.pagonxt.onetradefinance.integrations.apis.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.config.TestUtils;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.web.client.HttpServerErrorException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@UnitTest
class ApiErrorHandlerTest {

    @Spy
    private ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private ApiErrorHandler apiErrorHandler;

    @Test
    void handleError_bodyIsSantanderErrorResponse_throwsIntegrationException() {
        // Given
        String responseBody = TestUtils.getRawFile("/api-data/santander/santander-error-response.json");
        InputStream bodyStream = new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8));
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ClientHttpResponse clientHttpResponse = new MockClientHttpResponse(bodyStream, httpStatus);
        MediaType utf8MediaType = MediaType.parseMediaType("application/json;charset=UTF-8");
        clientHttpResponse.getHeaders().setContentType(utf8MediaType);

        // When and then
        Assertions.assertThrows(IntegrationException.class,
                () -> apiErrorHandler.handleError(clientHttpResponse, httpStatus),
                "Should throw IntegrationException");
    }

    @Test
    void handleError_bodyIsNotSantanderErrorResponse_throwsHttpServerErrorException() {
        // Given
        String responseBody = "Some error";
        InputStream bodyStream = new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8));
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ClientHttpResponse clientHttpResponse = new MockClientHttpResponse(bodyStream, httpStatus);

        // When and then
        Assertions.assertThrows(HttpServerErrorException.class,
                () -> apiErrorHandler.handleError(clientHttpResponse, httpStatus),
                "Should throw HttpServerErrorException");
    }
}
