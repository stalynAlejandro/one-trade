package com.pagonxt.onetradefinance.work.config;

import com.pagonxt.onetradefinance.work.configuration.SecurityConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class ControllerTestConfiguration {

    @MockBean
    SecurityConfiguration securityConfiguration;


}
