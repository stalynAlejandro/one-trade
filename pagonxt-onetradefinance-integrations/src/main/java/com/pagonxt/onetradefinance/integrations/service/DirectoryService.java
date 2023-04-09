package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.model.User;

/**
 * This interface provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get emails
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public interface DirectoryService {

    /**
     * This method allows getting recipients for notifications
     * @param initiatorId   : a string with the initiator id
     * @param notificationId: a string with the notification id
     * @return a string with the recipient
     */
    String getRecipientsForNotification(String initiatorId, String notificationId);

    /**
     * This method allows getting the email of the user
     * @param user a User object with a user
     * @see com.pagonxt.onetradefinance.integrations.model.User
     * @return a string with the user email
     */
    String getUserEmail(User user);

    /**
     * This method allows getting the user email of the office
     * @param userId a string with the user id
     * @return a string with the user email
     */
    String getOfficeUserEmail(String userId);

    /**
     * This method allows getting the email of the customer
     * @param customerId a string with the customer id
     * @return a string with the customer email
     */
    String getCustomerEmail(String customerId);


}
