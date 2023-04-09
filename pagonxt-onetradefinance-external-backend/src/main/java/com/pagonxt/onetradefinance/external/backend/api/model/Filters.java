package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.HashMap;

/**
 * This class prints a collection of filters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Filters extends HashMap<String, Filter> {

    /**
     * class method
     * @return a collection of filters
     */
    @Override
    public String toString() {
        return "Filters{} " + super.toString();
    }
}
