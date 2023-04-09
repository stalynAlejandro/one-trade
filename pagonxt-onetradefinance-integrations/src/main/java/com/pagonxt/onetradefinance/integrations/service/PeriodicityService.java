package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.Translation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get the periodicity
 * @author -
 * @version jdk-11.0.13L
 * @since jdk-11.0.13
 */
@Service
public class PeriodicityService {

    /**
     * A Logger object is used to log messages for a specific system or application component
     */
    private static final Logger LOG = LoggerFactory.getLogger(PeriodicityService.class);
    public static final String DEFAULT_LOCALE = "es_es";

    private Map<String, String> conceptPeriodicity;
    private List<Translation> translations;


    /**
     * constructor method
     */
    public PeriodicityService() {
        try (InputStream resourceStream = this.getClass().getClassLoader()
                .getResourceAsStream("periodicity-data/periodicity.json")) {
            conceptPeriodicity = new HashMap<>();
            conceptPeriodicity = new ObjectMapper().readValue(resourceStream, new TypeReference<>() {});

        } catch (Exception e) {
            LOG.warn("Error reading periodicity files", e);
        }
        try (InputStream resourceStreamTranslations = this.getClass().getClassLoader()
                .getResourceAsStream("periodicity-data/periodicity-translations.json")) {
            translations = new ArrayList<>();
            translations.addAll(new ObjectMapper().readValue(resourceStreamTranslations, new TypeReference<>() {}));
        } catch (Exception e) {
            LOG.warn("Error reading periodicity files", e);
        }
    }

    /**
     * class method to get the concept periodicity
     * @param conceptId a string with the concept id
     * @return a string with the concept periodicity (translated)
     */
    public String getConceptPeriodicity(String conceptId) {
        return getConceptPeriodicityTranslated(conceptId, DEFAULT_LOCALE);
    }

    /**
     * class method to translate a concept periodicity
     * @param conceptId a string with the concept id
     * @param locale a string with the locale value
     * @return a string with the concept periodicity (translated)
     */
    public String getConceptPeriodicityTranslated(String conceptId, String locale) {
        String periodicityKey = conceptPeriodicity.get(conceptId);
        if (periodicityKey == null) {
            return null;
        }
        Translation translationPeriodicity =
                translations.stream().filter(t -> t.getKeyName().equals(periodicityKey)).findFirst().orElse(null);
        if(translationPeriodicity == null) {
            return null;
        }
        return translationPeriodicity.getTranslations().get(locale);
    }

}
