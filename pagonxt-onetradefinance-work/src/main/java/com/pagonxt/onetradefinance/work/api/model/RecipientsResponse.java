package com.pagonxt.onetradefinance.work.api.model;

/**
 * Model class for responses
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class RecipientsResponse {

    //class attribute
    private String recipients;

    /**
     * constructor method
     * @param recipients : a string with the recipients
     */
    public RecipientsResponse(String recipients) {
        this.recipients = recipients;
    }

    /**
     * getter method
     * @return a string with the recipients
     */
    public String getRecipients() {
        return recipients;
    }

    /**
     * setter method
     * @param recipients a string with the recipients
     */
    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }
}
