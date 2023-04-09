package com.pagonxt.onetradefinance.work.configuration;

import com.flowable.core.common.api.security.SecurityScope;
import com.flowable.core.idm.api.PlatformGroup;
import com.flowable.core.idm.api.PlatformIdentityService;
import com.flowable.core.idm.api.PlatformUser;
import org.springframework.security.core.Authentication;

import java.util.Set;
import java.util.stream.Collectors;

public class CustomSecurityScope implements SecurityScope {

    final Authentication authentication;

    final PlatformIdentityService identityService;

    public CustomSecurityScope(Authentication authentication, PlatformIdentityService platformIdentityService) {
        this.authentication = authentication;
        this.identityService = platformIdentityService;
    }

    @Override
    public String getUserId() {
        return authentication.getName();
    }

    @Override
    public Set<String> getGroupKeys() {
        return identityService
                .createPlatformGroupQuery()
                .groupMember(authentication.getName())
                .list()
                .stream()
                .map(PlatformGroup::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public String getTenantId() {
        return null;
    }

    @Override
    public String getUserDefinitionKey() {
        String userDefinition = null;
        PlatformUser platformUser = identityService
                .createPlatformUserQuery()
                .userId(authentication.getName())
                .singleResult();
        if (platformUser != null) {
            userDefinition = platformUser.getUserDefinitionKey();
        }
        return userDefinition;
    }

    @Override
    public boolean hasAuthority(String authority) {
        return false;
    }
}
