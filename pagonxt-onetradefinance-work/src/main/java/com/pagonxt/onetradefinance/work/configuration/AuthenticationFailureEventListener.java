package com.pagonxt.onetradefinance.work.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureEventListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFailureEventListener.class);

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        LOG.warn("Received AbstractAuthenticationFailureEvent, Message: {}; ", event.getException().getMessage(), event.getException());
    }
}
