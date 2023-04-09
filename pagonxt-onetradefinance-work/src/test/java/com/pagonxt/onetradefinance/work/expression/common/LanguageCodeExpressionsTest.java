package com.pagonxt.onetradefinance.work.expression.common;

import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class LanguageCodeExpressionsTest {

    private static final String EXPECTED_DEFAULT_CODE = "es_es";

    private static final Map<String, String> VALID_LANGUAGE_CODES = Map.of(
            "ESP", EXPECTED_DEFAULT_CODE,
            "CHL", EXPECTED_DEFAULT_CODE,
            "MEX", EXPECTED_DEFAULT_CODE,
            "COL", EXPECTED_DEFAULT_CODE,
            "ARG", EXPECTED_DEFAULT_CODE,
            "GBR", "en_us",
            "DEU", "de_de",
            "POL", "pl_pl",
            "PRT", "pt_pt"
    );

    private final LanguageCodeExpressions languageCodeExpressions = new LanguageCodeExpressions();

    @ParameterizedTest
    @ValueSource(strings = {"ESP", "CHL", "MEX", "COL", "ARG", "GBR", "DEU", "POL", "PRT"})
    void getByCountry_handles_valid_codes(String code) {
        // Given

        // When and then
        String expectedResult = VALID_LANGUAGE_CODES.get(code);
        assertThat(languageCodeExpressions.getByCountry(code)).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @ValueSource(strings = {"esp", "chl", "mex", "col", "arg", "gbr", "deu", "pol", "prt"})
    void getByCountry_handles_lowercase_codes(String code) {
        // Given

        // When and then
        String expectedResult = VALID_LANGUAGE_CODES.get(code.toUpperCase());
        assertThat(languageCodeExpressions.getByCountry(code)).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "yo", "PI", "absurd", "-1"})
    void getByCountry_handles_invalid_codes(String code) {
        // Given

        // When and then
        assertThat(languageCodeExpressions.getByCountry(code)).isEqualTo(EXPECTED_DEFAULT_CODE);
    }
}
