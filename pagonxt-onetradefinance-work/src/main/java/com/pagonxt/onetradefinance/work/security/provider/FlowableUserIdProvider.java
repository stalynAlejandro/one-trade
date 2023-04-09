package com.pagonxt.onetradefinance.work.security.provider;

import com.flowable.core.spring.security.SecurityUtils;
import org.springframework.stereotype.Component;

/**
 * class that provides the user id
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.security.provider.UserIdProvider
 * @since jdk-11.0.13
 */
@Component
public class FlowableUserIdProvider implements UserIdProvider {

    /**
     * Method to get the id of the current user
     * @return a string with the id of the current user
     */
    @Override
    public String getCurrentUserId() {
        return SecurityUtils.getCurrentUserSecurityScope().getUserId();
    }
}
