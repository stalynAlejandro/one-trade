package com.pagonxt.onetradefinance.work.security;

import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.SecurityException;
import org.flowable.content.api.ContentItem;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Service class to provide some methods to check the security of documents
 *
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.utils.CaseUtils
 * @see CaseSecurityService
 * @since jdk-11.0.13
 */
@Service
@Profile("elasticless")
public class DocumentSecurityServiceElasticless implements DocumentSecurityService {

    /**
     * Method to check can read the document
     *
     * @param userInfo    : UserInfo object
     * @param contentItem : ContentItem object
     * @throws SecurityException: handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see org.flowable.content.api.ContentItem
     */
    public void checkReadDocument(UserInfo userInfo, ContentItem contentItem) throws SecurityException {
        // Elasticless implementation does nothing
    }

    /**
     * Method to check can edit the document
     *
     * @param userInfo    : UserInfo object
     * @param contentItem : ContentItem object
     * @throws SecurityException: handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see org.flowable.content.api.ContentItem
     */
    public void checkEditDocument(UserInfo userInfo, ContentItem contentItem) throws SecurityException {
        // Elasticless implementation does nothing
    }
}
