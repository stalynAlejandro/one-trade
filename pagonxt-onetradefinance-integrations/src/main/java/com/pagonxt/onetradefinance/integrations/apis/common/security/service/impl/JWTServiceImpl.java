package com.pagonxt.onetradefinance.integrations.apis.common.security.service.impl;

import com.pagonxt.onetradefinance.integrations.apis.common.security.service.JWTService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Profile("apigee-apis")
public class JWTServiceImpl implements JWTService {

    private final HttpServletRequest httpServletRequest;

    public JWTServiceImpl(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getToken() {
        String authorization = httpServletRequest.getHeader("Authorization");
        return authorization.substring(7);
    }
}
