package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.model.User;

import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.USER_TYPE_CUSTOMER;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.USER_TYPE_OFFICE;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get mocked emails
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class MockedDirectoryService implements DirectoryService {

    static final String MOCK_RECIPIENTS = "recipient1@test.com,recipient2@test.com";


	/**
	 * This method allows getting recipients for notifications
	 * @param initiatorId   : a string with the initiator id
	 * @param notificationId: a string with the notification id
	 * @return a string with the recipient
	 */
	@Override
    public String getRecipientsForNotification(String initiatorId, String notificationId) {

        return MOCK_RECIPIENTS;
    }

	/**
	 * This method allows getting the email of the user
	 * @param user a User object with a user
	 * @see com.pagonxt.onetradefinance.integrations.model.User
	 * @return a string with the user email
	 */
	@Override
	public String getUserEmail(User user) {
		if(user == null || user.getUserId() == null) {
			return null;
		}
		switch(user.getUserType()) {
			case USER_TYPE_OFFICE:
				return getOfficeUserEmail(user.getUserId());
			case USER_TYPE_CUSTOMER:
				return getCustomerEmail(user.getUserId());
			default:
				return user.getUserId().trim().concat("@mail.com");
		}
	}

	/**
	 * This method allows getting the user email of the office
	 * @param userId a string with the user id
	 * @return a string with the user email
	 */
	@Override
	public String getOfficeUserEmail(String userId) {
		if(userId == null) {
			return null;
		}
		return userId.trim().concat(".office-user@mail.com");
	}

	/**
	 * This method allows getting the email of the customer
	 * @param customerId a string with the customer id
	 * @return a string with the customer email
	 */
	@Override
	public String getCustomerEmail(String customerId) {
		if(customerId == null) {
			return null;
		}
		return customerId.trim().concat(".customer@mail.com");
	}

}
