package com.pagonxt.onetradefinance.work.configuration;

import com.flowable.core.common.api.security.SecurityScope;
import com.flowable.core.common.api.security.SecurityScopeProvider;
import com.flowable.core.idm.api.PlatformIdentityService;
import com.flowable.core.spring.security.FlowablePrincipalSecurityScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@Profile("santander | santander_local")
public class CustomSecurityScopeProvider implements SecurityScopeProvider {

    @Autowired
    PlatformIdentityService identityService;

    @Override
    public SecurityScope getSecurityScope(Principal principal) {
        if (principal instanceof Authentication) {
            return new CustomSecurityScope((Authentication) principal, identityService);
        } else {
            return new FlowablePrincipalSecurityScope(principal);
        }
    }
}
