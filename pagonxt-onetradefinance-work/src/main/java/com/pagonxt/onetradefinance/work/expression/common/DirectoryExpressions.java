package com.pagonxt.onetradefinance.work.expression.common;

import com.pagonxt.onetradefinance.integrations.configuration.IntegrationDirectoryProperties;
import com.pagonxt.onetradefinance.integrations.service.DirectoryService;
import com.pagonxt.onetradefinance.work.service.RecipientsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * service class for directory expressions
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.service.DirectoryService
 * @see com.pagonxt.onetradefinance.integrations.configuration.IntegrationDirectoryProperties
 * @see com.pagonxt.onetradefinance.work.service.RecipientsService
 * @since jdk-11.0.13
 */
@Service
public class DirectoryExpressions {

    //Class attributes
    private final com.pagonxt.onetradefinance.integrations.service.DirectoryService delegateDirectoryService;
    private final IntegrationDirectoryProperties integrationDirectoryProperties;
    private final RecipientsService recipientsService;

    /**
     * constructor method
     * @param delegateDirectoryService      : a DirectoryService object
     * @param integrationDirectoryProperties : an IntegrationDirectoryProperties object
     * @param recipientsService             :a RecipientService object
     */
    public DirectoryExpressions(DirectoryService delegateDirectoryService,
                                IntegrationDirectoryProperties integrationDirectoryProperties,
                                RecipientsService recipientsService) {
        this.delegateDirectoryService = delegateDirectoryService;
        this.integrationDirectoryProperties = integrationDirectoryProperties;
        this.recipientsService = recipientsService;
    }

    /**
     * This methods allows getting recipients for a notification
     * @param initiatorId     : a string with the initiator id
     * @param notificationId  : a string with the notification id
     * @return a string with recipients for a notification
     */
    public String getRecipientsForNotification(String initiatorId, String notificationId) {
        return delegateDirectoryService.getRecipientsForNotification(initiatorId, notificationId);
    }

    /**
     * This method allows to obtain the user email (office user)
     * @param userId : a string with the user id
     * @return a string with the user email
     */
    public String getOfficeUserEmail(String userId) {
        return StringUtils.defaultIfBlank(delegateDirectoryService.getOfficeUserEmail(userId),
                integrationDirectoryProperties.getFallbackOfficeUserEmail());
    }

    /**
     * This method allows to obtain the customer email
     * @param customerId : a string with the customer id
     * @return a string with the customer email
     */
    public String getCustomerEmail(String customerId) {
        return delegateDirectoryService.getCustomerEmail(customerId);
    }

    /**
     * This method allows to obtain the office email
     * @param officeCode : a string with the office code
     * @return a string with the office email
     */
    public String getOfficeEmail(String officeCode) {
        return StringUtils.defaultIfBlank(recipientsService.getOfficeEmail(officeCode),
                integrationDirectoryProperties.getFallbackOfficeEmail());
    }

    /**
     * This method allows to obtain the middle office email
     * @param middleOfficeId : a string with the middle office code
     * @return a string with the middle office email
     */
    public String getMiddleOfficeEmail(String middleOfficeId) {
        return StringUtils.defaultIfBlank(recipientsService.getMiddleOfficeEmail(middleOfficeId),
                integrationDirectoryProperties.getFallbackMiddleOfficeEmail());
    }
}
