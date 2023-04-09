package com.pagonxt.onetradefinance.work.expression.common;

import com.flowable.dataobject.engine.DataObjectEngine;
import com.flowable.dataobject.engine.impl.persistence.entity.MasterDataInstanceEntity;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@UnitTest
class MasterDataExpressionsTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DataObjectEngine dataObjectEngine;

    @InjectMocks
    private MasterDataExpressions masterDataExpressions;

    @Test
    void getInstanceName_ok() {
        // Given
        MasterDataInstanceEntity mockCountryEntity = Mockito.mock(MasterDataInstanceEntity.class);
        when(mockCountryEntity.getTranslations()).thenReturn(Map.of("en_uk", Map.of("name", "Spain")));
        when(dataObjectEngine.getDataObjectRuntimeService()
                .createMasterDataInstanceQuery()
                .definitionKey("md-country")
                .key("ESP")
                .includeTranslations()
                .singleResult()
        ).thenReturn(mockCountryEntity);

        // When
        String result = masterDataExpressions.getInstanceName("md-country", "ESP", "en_uk");

        // Then
        assertThat(result).isEqualTo("Spain");
    }

    @Test
    void getInstanceText_ok() {
        // Given
        MasterDataInstanceEntity mockCountryEntity = Mockito.mock(MasterDataInstanceEntity.class);
        when(mockCountryEntity.getTranslations()).thenReturn(Map.of("en_uk", Map.of("name", "Spain")));
        when(dataObjectEngine.getDataObjectRuntimeService()
                .createMasterDataInstanceQuery()
                .definitionKey("md-country")
                .key("ESP")
                .includeTranslations()
                .singleResult()
        ).thenReturn(mockCountryEntity);

        // When
        String result = masterDataExpressions.getInstanceText("md-country", "ESP", "en_uk", "name");

        // Then
        assertThat(result).isEqualTo("Spain");
    }

    @Test
    void getInstanceText_noInstance_throwsException() {
        // Given
        when(dataObjectEngine.getDataObjectRuntimeService()
                .createMasterDataInstanceQuery()
                .definitionKey("md-country")
                .key("FRA")
                .includeTranslations()
                .singleResult()
        ).thenReturn(null);

        // When and then
        Exception exception = assertThrows(NoSuchElementException.class,
                () -> masterDataExpressions.getInstanceText("md-country", "FRA", "en_uk", "name"));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Instance FRA not found for md-country master data definition");
    }

    @Test
    void getInstanceText_wrongLanguage_throwsException() {
        // Given
        MasterDataInstanceEntity mockCountryEntity = Mockito.mock(MasterDataInstanceEntity.class);
        when(mockCountryEntity.getTranslations()).thenReturn(Map.of("en_uk", Map.of("name", "Spain")));
        when(dataObjectEngine.getDataObjectRuntimeService()
                .createMasterDataInstanceQuery()
                .definitionKey("md-country")
                .key("ESP")
                .includeTranslations()
                .singleResult()
        ).thenReturn(mockCountryEntity);

        // When and then
        Exception exception = assertThrows(NoSuchElementException.class,
                () -> masterDataExpressions.getInstanceText("md-country", "ESP", "fr_fr", "name"));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Language fr_fr not found in md-country master data ESP instance translations");
    }

    @Test
    void getInstanceText_wrongKey_throwsException() {
        // Given
        MasterDataInstanceEntity mockCountryEntity = Mockito.mock(MasterDataInstanceEntity.class);
        when(mockCountryEntity.getTranslations()).thenReturn(Map.of("en_uk", Map.of("name", "Spain")));
        when(dataObjectEngine.getDataObjectRuntimeService()
                .createMasterDataInstanceQuery()
                .definitionKey("md-country")
                .key("ESP")
                .includeTranslations()
                .singleResult()
        ).thenReturn(mockCountryEntity);

        // When and then
        Exception exception = assertThrows(NoSuchElementException.class,
                () -> masterDataExpressions.getInstanceText("md-country", "ESP", "en_uk", "capital"));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Key capital not found in md-country master data ESP instance en_uk translation");
    }
}
