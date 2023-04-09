package com.pagonxt.onetradefinance.work.service.exception;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Class with some methods to handle exceptions when no such an element
 * @author -
 * @version jdk-11.0.13
 * @see java.util.NoSuchElementException
 * @since jdk-11.0.13
 */
public class TemplateVariationNotFoundException extends NoSuchElementException {

    /**
     * constructor method
     * @param template  : a string with the template
     * @param variant   : a collection of variants
     */
    public TemplateVariationNotFoundException(String template, Map<String, Object> variant) {
        super(String.format("Variation not found for template %s with arguments %s", template, variant));
    }
}
