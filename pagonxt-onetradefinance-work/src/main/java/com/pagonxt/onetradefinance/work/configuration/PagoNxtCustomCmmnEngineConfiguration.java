package com.pagonxt.onetradefinance.work.configuration;

import com.flowable.spring.boot.EngineConfigurationConfigurer;
import com.pagonxt.onetradefinance.logger.LoggerWrapper;
import com.pagonxt.onetradefinance.work.listener.cmmn.PagoNxtCompletePlanItemInstanceLifecycleListener;
import com.pagonxt.onetradefinance.work.listener.cmmn.PagoNxtTerminalStateCaseObservabilityLifecycleListener;
import org.flowable.cmmn.api.runtime.PlanItemDefinitionType;
import org.flowable.cmmn.spring.SpringCmmnEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;


/**
 * Configuration class
 * This can make development faster and easier by eliminating the need
 * to define explicit lifecycle listeners for case instances.
 *
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
public class PagoNxtCustomCmmnEngineConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(PagoNxtCustomCmmnEngineConfiguration.class);
    /**
     * Class method
     *
     * @return an SpringCmmnEngineConfiguration
     */
    @Bean
    @DependsOn({"loggerWrapper"})
    public EngineConfigurationConfigurer<SpringCmmnEngineConfiguration> pagoNxtCustomCmmnEngineConfigurer
    (@Autowired LoggerWrapper loggerWrapper) {
        return engineConfiguration -> {
            LOG.info("Adding PagoNxt custom CaseInstanceLifeCycleListener");
            engineConfiguration.addCaseInstanceLifeCycleListener(
                    new PagoNxtTerminalStateCaseObservabilityLifecycleListener(loggerWrapper)
            );
            LOG.info("Adding PagoNxt custom PlanItemInstanceLifeCycleListener for milestones");
            engineConfiguration.addPlanItemInstanceLifeCycleListener(PlanItemDefinitionType.MILESTONE,
                    new PagoNxtCompletePlanItemInstanceLifecycleListener(loggerWrapper,
                            engineConfiguration.getCmmnRuntimeService()));
        };
    }


}

