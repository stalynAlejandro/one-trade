package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.CollectionType;
import com.pagonxt.onetradefinance.integrations.model.OperationType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class OperationTypeServiceTest {

    @Mock
    ObjectMapper mapper;

    @InjectMocks
    OperationTypeService operationTypeService;

    @Test
    void getOperationTypeByProduct_isOk_returnsValidData() throws IOException {
        // Given
        TypeReference<List<OperationType>> listTypeReference = new TypeReference<>() {};
        List<OperationType> expectedResult = new ObjectMapper().readValue(new ClassPathResource("operation-type-data/operation-types.json").getFile(), listTypeReference);
        // When
        List<OperationType> result = operationTypeService.getOperationTypeByProduct("CLE");
        // Then
        assertEquals(expectedResult, result, "Should be the same");
    }

    @Test
    void exists_keyExist_returnsTrue() {
        // Given & When
        boolean result = operationTypeService.exists("exportUnpaid");
        // Then
        assertTrue(result, "Result should be true");
    }

    @Test
    void exists_keyExist_returnsFalse() {
        // Given & When
        boolean result = operationTypeService.exists("fakeId");
        // Then
        assertFalse(result, "Result should be false");
    }
}
