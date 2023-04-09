package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.OperationTypeDto;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.OperationType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UnitTest
class OperationTypeDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private OperationTypeDtoSerializer serializer;

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        OperationType operationType = mapper.readValue(new ClassPathResource("data/model/operationType.json").getFile(), OperationType.class);
        OperationTypeDto expectedOperationTypeDto = mapper.readValue(new ClassPathResource("data/dto/operationType-dto.json").getFile(), OperationTypeDto.class);
        // When
        OperationTypeDto result = serializer.toDto(operationType);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedOperationTypeDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        OperationTypeDto result = serializer.toDto(null);
        // Then
        assertNull(result);
    }

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        OperationType expectedOperationType = mapper.readValue(new ClassPathResource("data/model/operationType.json").getFile(), OperationType.class);
        OperationTypeDto operationTypeDto = mapper.readValue(new ClassPathResource("data/dto/operationType-dto.json").getFile(), OperationTypeDto.class);
        // When
        OperationType result = serializer.toModel(operationTypeDto);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedOperationType);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        OperationType expectedResult = new OperationType();
        // When
        OperationType result = serializer.toModel(null);
        // Then
        assertEquals(expectedResult, result);
    }
}
