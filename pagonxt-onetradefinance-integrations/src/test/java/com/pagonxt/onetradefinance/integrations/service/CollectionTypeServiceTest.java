package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.CollectionType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@UnitTest
class CollectionTypeServiceTest {

    @Mock
    ObjectMapper mapper;

    @InjectMocks
    CollectionTypeService collectionTypeService;

    @Test
    void getCollectionTypeByProduct_isOk_returnsValidData() throws IOException {
        // Given
        TypeReference<List<CollectionType>> listTypeReference = new TypeReference<>() {};
        List<CollectionType> expectedResult = new ObjectMapper().readValue(new ClassPathResource("collection-type-data/collection-types.json").getFile(), listTypeReference);
        // When
        List<CollectionType> result = collectionTypeService.getCollectionType("CLE", "EUR");
        // Then
        assertEquals(expectedResult, result, "Should be the same");
    }

    @Test
    void exists_keyExist_returnsTrue() {
        // Given & When
        boolean result = collectionTypeService.exists("exportSimpleSpecialManagement");
        // Then
        assertTrue(result, "Result should be true");
    }

    @Test
    void exists_keyExist_returnsFalse() {
        // Given & When
        boolean result = collectionTypeService.exists("fakeId");
        // Then
        assertFalse(result, "Result should be false");
    }

    @Test
    void getIdByKey_keyExist_returnsFalse() throws IOException {
        // Given
        // When
        String result = collectionTypeService.getIdByKey("exportSimpleSpecialManagement");
        // Then
        assertEquals("6556010000001", result, "Result should be false");
    }
}