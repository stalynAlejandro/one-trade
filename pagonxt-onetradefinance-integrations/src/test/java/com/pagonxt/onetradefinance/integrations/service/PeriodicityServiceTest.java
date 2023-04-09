package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class PeriodicityServiceTest {
    @InjectMocks
    PeriodicityService periodicityService;

    @ParameterizedTest
    @CsvSource(value = {"B70, mensual",
                        "B66, trimestral",
                        "C82, mensual",
                        "NotFoundConcept, null",
                        "null, null"}
                        , nullValues={"null"})
    void getConceptPeriodicity_ok_returnsValidData(String conceptId, String periodicity) {
        // When
        String result = periodicityService.getConceptPeriodicity(conceptId);
        // Then
        assertEquals(periodicity, result);
    }

    @ParameterizedTest
    @CsvSource(value = {"es_es, mensual",
                        "en_us, monthly",
                        "notFoundLocale, null",
                        "null, null"}
                        , nullValues={"null"})
    void getConceptPeriodicityTranslated_ok_returnsValidData(String locale, String periodicity) {
        // When
        String result = periodicityService.getConceptPeriodicityTranslated("B70", locale);
        // Then
        assertEquals(periodicity, result);
    }
}
