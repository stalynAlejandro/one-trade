package com.pagonxt.onetradefinance.work.security;

import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.SecurityException;
import com.pagonxt.onetradefinance.work.service.model.Case;
import com.pagonxt.onetradefinance.work.utils.CaseUtils;
import org.flowable.content.api.ContentItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Profile("!elasticless")
public class DocumentSecurityServiceImpl implements DocumentSecurityService {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(DocumentSecurityServiceImpl.class);

    //Class attributes
    private final CaseSecurityService caseSecurityService;
    private final CaseUtils caseUtils;

    /**
     * Constructor method
     *
     * @param caseSecurityService : a CaseSecurityService object
     * @param caseUtils           : a CaseUtils object
     */
    public DocumentSecurityServiceImpl(CaseSecurityService caseSecurityService,
                                       CaseUtils caseUtils) {
        this.caseSecurityService = caseSecurityService;
        this.caseUtils = caseUtils;
    }

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
        try {
            caseSecurityService.checkRead(userInfo, getDocumentCase(contentItem));
        } catch (SecurityException e) {
            LOG.error("The user does not have permission to read the document", e);
            throw new SecurityException(userInfo.getUser().getUserId(), contentItem.getId(), "document");
        }
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
        try {
            caseSecurityService.checkEdit(userInfo, getDocumentCase(contentItem));
        } catch (SecurityException e) {
            LOG.error("The user does not have permission to edit the document", e);
            throw new SecurityException(userInfo.getUser().getUserId(), contentItem.getId(), "document");
        }
    }

    /**
     * Method to get the document of case
     *
     * @param contentItem : ContentItem object
     * @see org.flowable.content.api.ContentItem
     */
    private Case getDocumentCase(ContentItem contentItem) {
        return caseUtils.getCaseById(caseUtils.getDocumentCaseId(contentItem));
    }
}
