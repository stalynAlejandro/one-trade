package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.HashMap;

/**
 * This class prints a collection of filter definitions
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class FiltersDefinition extends HashMap<String, FilterDefinition> {

    /**
     * class method
     * @return a collection of filters definitions
     */
    @Override
    public String toString() {
        return "FiltersDefinition{} " + super.toString();
    }
}
