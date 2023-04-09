package com.pagonxt.onetradefinance.work.security;


import com.flowable.platform.service.task.PlatformTaskService;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.SecurityException;
import com.pagonxt.onetradefinance.work.utils.CaseUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Elastic-less implementation of the CaseSecurityService
 *
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.security.CaseSecurityServiceImpl
 * @see com.pagonxt.onetradefinance.work.utils.CaseUtils
 * @see com.flowable.platform.service.task.PlatformTaskService
 * @since jdk-11.0.13
 */
@Service
@Profile("elasticless")
public class CaseSecurityServiceElasticless extends CaseSecurityServiceImpl {

    /**
     * Constructor
     *
     * @param caseUtils           : case utils
     * @param platformTaskService : platform task service
     */
    public CaseSecurityServiceElasticless(CaseUtils caseUtils, PlatformTaskService platformTaskService) {
        super(caseUtils, platformTaskService);
    }

    /**
     * Method to check read
     *
     * @param userInfo : UserInfo object
     * @param caseCode : a string with the case code
     * @throws SecurityException : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    @Override
    public void checkRead(UserInfo userInfo, String caseCode) throws SecurityException {
        // Elasticless implementation does nothing
    }

    /**
     * Method to check edit
     *
     * @param userInfo : UserInfo object
     * @param caseCode : a string with the case code
     * @throws SecurityException : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    @Override
    public void checkEdit(UserInfo userInfo, String caseCode) throws SecurityException {
        // Elasticless implementation does nothing
    }
}
