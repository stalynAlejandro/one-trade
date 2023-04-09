package com.pagonxt.onetradefinance.work.security;

import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.flowable.platform.service.task.PlatformTaskService;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.SecurityException;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.service.model.Case;
import com.pagonxt.onetradefinance.work.utils.CaseUtils;
import com.pagonxt.onetradefinance.work.utils.CaseVariableUtils;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.*;
import static com.pagonxt.onetradefinance.work.common.CaseCommonConstants.VARNAME_PRODUCT;

/**
 * Service class to provide some methods to check the security od cases
 *
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.utils.CaseUtils
 * @see com.flowable.platform.service.task.PlatformTaskService
 * @since jdk-11.0.13
 */
@Service
@Profile("!elasticless")
public class CaseSecurityServiceImpl implements CaseSecurityService {

    private static final String UNKNOWN = "unknown";

    //Class attributes
    private final CaseUtils caseUtils;
    private final PlatformTaskService platformTaskService;

    /**
     * constructor method
     *
     * @param caseUtils           : a CaseUtils object
     * @param platformTaskService : a PlatformTaskService object
     */
    public CaseSecurityServiceImpl(CaseUtils caseUtils, PlatformTaskService platformTaskService) {
        this.caseUtils = caseUtils;
        this.platformTaskService = platformTaskService;
    }

    /**
     * Method to check read
     *
     * @param userInfo : UserInfo object
     * @param caseCode : a string with the case code
     * @throws SecurityException : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    public void checkRead(UserInfo userInfo, String caseCode) throws SecurityException {
        checkRead(userInfo, caseUtils.getCaseByCode(caseCode));
    }

    /**
     * Method to check edit
     *
     * @param userInfo : UserInfo object
     * @param caseCode : a string with the case code
     * @throws SecurityException : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    public void checkEdit(UserInfo userInfo, String caseCode) throws SecurityException {
        checkEdit(userInfo, caseUtils.getCaseByCode(caseCode));
    }

    /**
     * Method to check read
     *
     * @param userInfo     : UserInfo object
     * @param caseInstance : a Case object
     * @throws SecurityException : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     */
    public void checkRead(UserInfo userInfo, Case caseInstance) throws SecurityException {
        if (!canRead(userInfo, caseInstance)) {
            throwSecurityException(userInfo, caseInstance);
        }
    }

    /**
     * Method to check edit
     *
     * @param userInfo     : UserInfo object
     * @param caseInstance : a Case object
     * @throws SecurityException : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     */
    public void checkEdit(UserInfo userInfo, Case caseInstance) throws SecurityException {
        if (!canEdit(userInfo, caseInstance)) {
            throwSecurityException(userInfo, caseInstance);
        }
    }

    /**
     * Method to check read
     *
     * @param userInfo     : UserInfo object
     * @param caseInstance : a Case object
     * @throws SecurityException : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     */
    public void checkRead(UserInfo userInfo, CaseInstance caseInstance) throws SecurityException {
        if (!canRead(userInfo, caseInstance)) {
            throwSecurityException(userInfo, caseInstance);
        }
    }

    /**
     * Method to check edit
     *
     * @param userInfo     : UserInfo object
     * @param caseInstance : a Case object
     * @throws SecurityException : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     */
    public void checkEdit(UserInfo userInfo, CaseInstance caseInstance) throws SecurityException {
        if (!canEdit(userInfo, caseInstance)) {
            throwSecurityException(userInfo, caseInstance);
        }
    }

    /**
     * Method to check read
     *
     * @param userInfo      : UserInfo object
     * @param caseVariables : Collection with case variables
     * @throws SecurityException : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    public void checkRead(UserInfo userInfo, Map<String, Object> caseVariables) throws SecurityException {
        containsData(caseVariables);
        if (!canRead(userInfo, caseVariables)) {
            throwCaseSecurityException(userInfo, caseVariables);
        }
    }

    /**
     * Method to check read task
     *
     * @param userInfo : UserInfo object
     * @param taskId   : a string with the task id
     * @throws SecurityException : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    public void checkReadTask(UserInfo userInfo, String taskId) throws SecurityException {
        checkReadTask(userInfo, platformTaskService.getTaskVariables(taskId));
    }

    /**
     * Method to check edit task
     *
     * @param userInfo : UserInfo object
     * @param taskId   : a string with the task id
     * @throws SecurityException : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    public void checkEditTask(UserInfo userInfo, String taskId) throws SecurityException {
        checkEditTask(userInfo, platformTaskService.getTaskVariables(taskId));
    }

    /**
     * Method to check read task
     *
     * @param userInfo      : UserInfo object
     * @param taskVariables : a collection of task variables
     * @throws SecurityException : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    public void checkReadTask(UserInfo userInfo, Map<String, Object> taskVariables) throws SecurityException {
        containsDataTask(taskVariables);
        if (!canReadTask(userInfo, taskVariables)) {
            throwTaskSecurityException(userInfo, taskVariables);
        }
    }

    /**
     * Method to check edit task
     *
     * @param userInfo      : UserInfo object
     * @param taskVariables : a collection of task variables
     * @throws SecurityException : Handles security exceptions
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    public void checkEditTask(UserInfo userInfo, Map<String, Object> taskVariables) throws SecurityException {
        containsDataTask(taskVariables);
        if (!canEditTask(userInfo, taskVariables)) {
            throwTaskSecurityException(userInfo, taskVariables);
        }
    }


    /**
     * Method to check can read
     *
     * @param userInfo     : UserInfo object
     * @param caseInstance : a Case object
     * @return a boolean value
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     */
    public boolean canRead(UserInfo userInfo, Case caseInstance) {
        if (caseInstance == null) {
            return false;
        }
        String caseOffice = CaseVariableUtils.getString(caseInstance, OPERATION_OFFICE);
        String caseMiddleOffice = CaseVariableUtils.getString(caseInstance, OPERATION_MIDDLE_OFFICE);
        return canRead(userInfo, caseOffice, caseMiddleOffice);
    }

    /**
     * Method to check can edit
     *
     * @param userInfo     : UserInfo object
     * @param caseInstance : a Case object
     * @return a boolean value
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     */
    public boolean canEdit(UserInfo userInfo, Case caseInstance) {
        return canRead(userInfo, caseInstance);
    }

    /**
     * Method to check can read
     *
     * @param userInfo     : UserInfo object
     * @param caseInstance : a CaseInstance object
     * @return a boolean value
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see org.flowable.cmmn.api.runtime.CaseInstance
     */
    public boolean canRead(UserInfo userInfo, CaseInstance caseInstance) {
        if (caseInstance == null) {
            return false;
        }
        DataObjectInstanceVariableContainerImpl operation = (DataObjectInstanceVariableContainerImpl) caseInstance
                .getCaseVariables().get(OPERATION);
        String caseOffice = operation == null ? null : operation.getString(REQUEST_OFFICE);
        String caseMiddleOffice = operation == null ? null : operation.getString(REQUEST_MIDDLE_OFFICE);
        return canRead(userInfo, caseOffice, caseMiddleOffice);
    }

    /**
     * Method to check can edit
     *
     * @param userInfo     : UserInfo object
     * @param caseInstance : a CaseInstance object
     * @return a boolean value
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see org.flowable.cmmn.api.runtime.CaseInstance
     */
    public boolean canEdit(UserInfo userInfo, CaseInstance caseInstance) {
        return canRead(userInfo, caseInstance);
    }

    /**
     * Method to check can read task
     *
     * @param userInfo      : UserInfo object
     * @param taskVariables : a collection of task variables
     * @return a boolean value
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    public boolean canReadTask(UserInfo userInfo, Map<String, Object> taskVariables) {
        Map<String, Object> operation = (Map<String, Object>) ((Map<String, Object>) taskVariables.get(ROOT))
                .get(OPERATION);
        String caseOffice = (String) operation.get(REQUEST_OFFICE);
        String caseMiddleOffice = (String) operation.get(REQUEST_MIDDLE_OFFICE);
        return canRead(userInfo, caseOffice, caseMiddleOffice);
    }

    /**
     * Method to check can edit task
     *
     * @param userInfo  : UserInfo object
     * @param operation : a collection of operations
     * @return a boolean value
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    public boolean canEditTask(UserInfo userInfo, Map<String, Object> operation) {
        return canReadTask(userInfo, operation);
    }

    /**
     * Method to check can read
     *
     * @param userInfo         : UserInfo object
     * @param caseOffice       : a string with the case office
     * @param caseMiddleOffice : a string with the case middle Office
     * @return a boolean value
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    private boolean canRead(UserInfo userInfo, String caseOffice, String caseMiddleOffice) {
        if (userInfo == null || userInfo.getUser() == null || userInfo.getUser().getUserType() == null) {
            return false;
        }
        boolean result = false;
        switch (userInfo.getUser().getUserType()) {
            case USER_TYPE_CUSTOMER: {
                // TODO a la espera de que Vicente nos explique la relación entre usuarios y clientes para implementar este caso
                // TODO a la espera de que Vicente nos explique la relación entre usuarios
                //  y clientes para implementar este caso
                result = true;
            }
            break;
            case USER_TYPE_OFFICE: {
                result = userInfo.getOffice() != null && userInfo.getOffice().equals(caseOffice);
            }
            break;
            case USER_TYPE_MIDDLE_OFFICE: {
                result = userInfo.getMiddleOffice() != null && userInfo.getMiddleOffice().equals(caseMiddleOffice);
            }
            break;
            case USER_TYPE_BACKOFFICE: {
                result = true;
            }
            break;
            default:
        }
        return result;
    }

    /**
     * Method to check can read
     * @param userInfo              : UserInfo object
     * @param caseVariables         : a collection of case variables
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return a boolean value
     */
    public boolean canRead(UserInfo userInfo, Map<String, Object> caseVariables) {
        DataObjectInstanceVariableContainerImpl operation = (DataObjectInstanceVariableContainerImpl) caseVariables
                .get(OPERATION);
        String caseOffice = operation.getString(REQUEST_OFFICE);
        String caseMiddleOffice = operation.getString(REQUEST_MIDDLE_OFFICE);
        return canRead(userInfo, caseOffice, caseMiddleOffice);
    }

    /**
     * Method to throw Security Exceptions
     *
     * @param userInfo     : UserInfo object
     * @param caseInstance : a Case object
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     */
    private void throwSecurityException(UserInfo userInfo, Case caseInstance) {
        String code = CaseVariableUtils.getString(caseInstance, OPERATION_CODE);
        String product = CaseVariableUtils.getString(caseInstance, VARNAME_PRODUCT);
        throw new SecurityException(userInfo.getUser().getUserId(),
                code == null ? UNKNOWN : code,
                product == null ? UNKNOWN : product);
    }

    /**
     * Method to throw Security Exceptions
     *
     * @param userInfo     : UserInfo object
     * @param caseInstance : a CaseInstance object
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see org.flowable.cmmn.api.runtime.CaseInstance
     */
    private void throwSecurityException(UserInfo userInfo, CaseInstance caseInstance) {
        DataObjectInstanceVariableContainerImpl operation = (DataObjectInstanceVariableContainerImpl) caseInstance
                .getCaseVariables().get(OPERATION);
        String code = operation == null ? UNKNOWN : operation.getString(REQUEST_CODE);
        String product = (String) caseInstance.getCaseVariables().get(VARNAME_PRODUCT);
        throw new SecurityException(userInfo.getUser().getUserId(),
                code == null ? UNKNOWN : code,
                product == null ? UNKNOWN : product);
    }

    /**
     * Method to throw Security Exceptions
     *
     * @param userInfo      : UserInfo object
     * @param taskVariables : a collection of task variables
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    private static void throwTaskSecurityException(UserInfo userInfo, Map<String, Object> taskVariables) {
        Map<String, Object> root = (Map<String, Object>) taskVariables.get(ROOT);
        Map<String, Object> operation = (Map<String, Object>) root.get(OPERATION);
        String code = (String) operation.get(REQUEST_CODE);
        String product = (String) root.get(VARNAME_PRODUCT);
        throw new SecurityException(userInfo.getUser().getUserId(), code, product);
    }

    /**
     * Method to throw Security Exceptions
     *
     * @param userInfo      : UserInfo object
     * @param caseVariables : a collection of case variables
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    private static void throwCaseSecurityException(UserInfo userInfo, Map<String, Object> caseVariables) {
        DataObjectInstanceVariableContainerImpl operation = (DataObjectInstanceVariableContainerImpl) caseVariables
                .get(OPERATION);
        String code = operation.getString(REQUEST_CODE);
        String product = operation.getString(VARNAME_PRODUCT);
        throw new SecurityException(userInfo.getUser().getUserId(), code, product);
    }

    /**
     * Method to check task data
     *
     * @param taskVariables: a collection of task variables
     */
    private void containsDataTask(Map<String, Object> taskVariables) {
        if (taskVariables != null) {
            Map<String, Object> root = (Map<String, Object>) taskVariables.get(ROOT);
            if (root != null) {
                Map<String, Object> operation = (Map<String, Object>) root.get(OPERATION);
                if (operation != null) {
                    return;
                }
            }
        }
        throw new ResourceNotFoundException("No task data found", "containsDataTask");
    }

    /**
     * Method to check data
     *
     * @param caseVariables: a collection of case variables
     */
    private void containsData(Map<String, Object> caseVariables) {
        if (caseVariables != null) {
            DataObjectInstanceVariableContainerImpl operation = (DataObjectInstanceVariableContainerImpl) caseVariables
                    .get(OPERATION);
            if (operation != null) {
                return;
            }
        }
        throw new ResourceNotFoundException("No case data found", "containsData");
    }
}
