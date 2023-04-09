package com.pagonxt.onetradefinance.work.users.bot;

import java.util.HashMap;

/**
 * class for responses
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Response extends HashMap<String, String> {

    /**
     * Method to set a response
     * @param language  : a string object with the language
     * @param value     : a string object with the value
     */
    public void setResponse(String language, String value) {
        this.put(language, value);
    }

    /**
     * Method to set success
     * @param success : a boolean object with success value
     */
    public void setSuccess(Boolean success) {

        this.put("success", success.toString());
    }
}
