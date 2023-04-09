package com.pagonxt.onetradefinance.work.expression.common;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * service class for language code expressions
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Service
public class LanguageCodeExpressions {

    private static final String DEFAULT_CODE = "es_es";

    private static final Map<String, String> CODES = Map.of(
            "ESP", DEFAULT_CODE,
            "CHL", DEFAULT_CODE,
            "MEX", DEFAULT_CODE,
            "COL", DEFAULT_CODE,
            "ARG", DEFAULT_CODE,
            "GBR", "en_us",
            "DEU", "de_de",
            "POL", "pl_pl",
            "PRT", "pt_pt"
    );

    /**
     * Method to get a language code by country
     * @param country a string with the country
     * @return a string with the language code
     */
    public String getByCountry(String country) {
        return country==null ? DEFAULT_CODE : CODES.getOrDefault(country.toUpperCase(), DEFAULT_CODE);
    }
}
