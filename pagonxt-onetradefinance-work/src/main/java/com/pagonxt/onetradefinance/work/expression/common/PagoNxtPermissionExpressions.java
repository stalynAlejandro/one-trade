package com.pagonxt.onetradefinance.work.expression.common;

import org.apache.commons.lang.StringUtils;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;

/**
 * service class for PagoNxt expression exceptions
 * @author -
 * @version jdk-11.0.13
 * @see org.flowable.cmmn.api.CmmnRuntimeService
 * @since jdk-11.0.13
 */
@Service
public class PagoNxtPermissionExpressions {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(PagoNxtPermissionExpressions.class);

    /**
     * class attribute
     */
    private final CmmnRuntimeService cmmnRuntimeService;

    /**
     * constructor method
     * @param cmmnRuntimeService : a CmmnRuntimeService object
     */
    public PagoNxtPermissionExpressions(CmmnRuntimeService cmmnRuntimeService) {
        this.cmmnRuntimeService = cmmnRuntimeService;
    }

    /**
     * This method allows to get the group name from a resolution group
     * @param caseInstanceId : a string with the case instance id
     * @param resolutionGroup: a string with the resolution group
     * @return a string with the group name
     */
    public String getFlwGroupNameFromPgnNxtResolutionGroup(String caseInstanceId, String resolutionGroup) {
        if (StringUtils.isNotBlank(resolutionGroup)) {
            //Recover needed root case instance variables
            try {
                String product = cmmnRuntimeService.getVariable(caseInstanceId, PRODUCT).toString();
                String event = cmmnRuntimeService.getVariable(caseInstanceId, EVENT).toString();
                String country = cmmnRuntimeService.getVariable(caseInstanceId, COUNTRY).toString();
                return String.format("grpRes_%s_%s_%s_%s", country, product, event,resolutionGroup);
            } catch (Exception e) {
                LOG.error("Error recovering some mandatory case variable", e);
                throw new NoSuchElementException(String.format("Error recovering some mandatory case variable:  %s, %s, %s", PRODUCT, EVENT, COUNTRY));
            }
        } else{
            return null;
        }
    }
}
