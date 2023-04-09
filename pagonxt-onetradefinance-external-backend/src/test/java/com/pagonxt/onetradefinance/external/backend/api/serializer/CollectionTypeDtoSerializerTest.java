package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.CollectionTypeDto;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.CollectionType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UnitTest
class CollectionTypeDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private
    CollectionTypeDtoSerializer serializer;

    @Test
    void toDto_ok_returnsValidModel() throws JsonProcessingException {
        // Given
        CollectionTypeDto expectedCollectionType = new CollectionTypeDto("6556010000003", "Remesas de exportacion gestion electronica","CLE", null);
        CollectionType collectionType = new CollectionType("CLE", "6556010000003", "Remesas de exportacion gestion electronica", null, List.of("EUR", "USA"));
        // When
        CollectionTypeDto result = serializer.toDto(collectionType);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedCollectionType);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        CollectionTypeDto result = serializer.toDto(null);
        // Then
        assertNull(result);
    }
}
