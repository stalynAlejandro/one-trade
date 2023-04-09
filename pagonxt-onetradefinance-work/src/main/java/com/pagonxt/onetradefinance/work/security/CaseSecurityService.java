package com.pagonxt.onetradefinance.work.security;

import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.SecurityException;
import com.pagonxt.onetradefinance.work.service.model.Case;
import org.flowable.cmmn.api.runtime.CaseInstance;

import java.util.Map;

/**
 * Interface to provide some methods to check the security of cases
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.utils.CaseUtils
 * @see com.flowable.platform.service.task.PlatformTaskService
 * @since jdk-11.0.13
 */
public interface CaseSecurityService {

    /**
     * Method to check read
     * @param userInfo              : UserInfo object
     * @param caseCode              : a string with the case code
     * @throws SecurityException    : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    void checkRead(UserInfo userInfo, String caseCode) throws SecurityException;

    /**
     * Method to check edit
     * @param userInfo              : UserInfo object
     * @param caseCode              : a string with the case code
     * @throws SecurityException    : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    void checkEdit(UserInfo userInfo, String caseCode) throws SecurityException;

    /**
     * Method to check read
     * @param userInfo              : UserInfo object
     * @param caseInstance          : a Case object
     * @throws SecurityException    : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     */
    void checkRead(UserInfo userInfo, Case caseInstance) throws SecurityException;

    /**
     * Method to check edit
     * @param userInfo              : UserInfo object
     * @param caseInstance          : a Case object
     * @throws SecurityException    : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     */
    void checkEdit(UserInfo userInfo, Case caseInstance) throws SecurityException;

    /**
     * Method to check read
     * @param userInfo              : UserInfo object
     * @param caseInstance          : a Case object
     * @throws SecurityException    : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     */
    void checkRead(UserInfo userInfo, CaseInstance caseInstance) throws SecurityException;

    /**
     * Method to check edit
     * @param userInfo              : UserInfo object
     * @param caseInstance          : a Case object
     * @throws SecurityException    : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     */
    void checkEdit(UserInfo userInfo, CaseInstance caseInstance) throws SecurityException;

    /**
     * Method to check read
     * @param userInfo              : UserInfo object
     * @param caseVariables         : Collection with case variables
     * @throws SecurityException    : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    void checkRead(UserInfo userInfo, Map<String, Object> caseVariables) throws SecurityException;

    /**
     * Method to check read task
     * @param userInfo              : UserInfo object
     * @param taskId                : a string with the task id
     * @throws SecurityException    : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    void checkReadTask(UserInfo userInfo, String taskId) throws SecurityException;

    /**
     * Method to check edit task
     * @param userInfo              : UserInfo object
     * @param taskId                : a string with the task id
     * @throws SecurityException    : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    void checkEditTask(UserInfo userInfo, String taskId) throws SecurityException;

    /**
     * Method to check read task
     * @param userInfo              : UserInfo object
     * @param taskVariables         : a collection of task variables
     * @throws SecurityException    : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    void checkReadTask(UserInfo userInfo, Map<String, Object> taskVariables) throws SecurityException;

    /**
     * Method to check edit task
     * @param userInfo              : UserInfo object
     * @param taskVariables         : a collection of task variables
     * @throws SecurityException    : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    void checkEditTask(UserInfo userInfo, Map<String, Object> taskVariables) throws SecurityException;

    /**
     * Method to check can read
     * @param userInfo              : UserInfo object
     * @param caseInstance          : a Case object
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     * @return a boolean value
     */
    boolean canRead(UserInfo userInfo, Case caseInstance);

    /**
     * Method to check can edit
     * @param userInfo              : UserInfo object
     * @param caseInstance          : a Case object
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     * @return a boolean value
     */
    boolean canEdit(UserInfo userInfo, Case caseInstance);

    /**
     * Method to check can read
     * @param userInfo              : UserInfo object
     * @param caseInstance          : a CaseInstance object
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see org.flowable.cmmn.api.runtime.CaseInstance
     * @return a boolean value
     */
    boolean canRead(UserInfo userInfo, CaseInstance caseInstance);

    /**
     * Method to check can edit
     * @param userInfo              : UserInfo object
     * @param caseInstance          : a CaseInstance object
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see org.flowable.cmmn.api.runtime.CaseInstance
     * @return a boolean value
     */
    boolean canEdit(UserInfo userInfo, CaseInstance caseInstance);

    /**
     * Method to check can read task
     * @param userInfo              : UserInfo object
     * @param taskVariables         : a collection of task variables
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return a boolean value
     */
    boolean canReadTask(UserInfo userInfo, Map<String, Object> taskVariables);

    /**
     * Method to check can edit task
     * @param userInfo              : UserInfo object
     * @param operation             : a collection of operations
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return a boolean value
     */
    boolean canEditTask(UserInfo userInfo, Map<String, Object> operation);

    /**
     * Method to check can read
     * @param userInfo              : UserInfo object
     * @param caseOffice            : a string with the case office
     * @param caseMiddleOffice      : a string with the case middle Office
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return a boolean value
     */
    boolean canRead(UserInfo userInfo, Map<String, Object> caseVariables);
}
