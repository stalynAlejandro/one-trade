package com.pagonxt.onetradefinance.work.configuration;

import com.flowable.spring.boot.EngineConfigurationConfigurer;
import com.pagonxt.onetradefinance.work.parser.bpmn.usertask.PagoNxtBpmnUserTaskParseHandler;
import org.flowable.engine.parse.BpmnParseHandler;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class
 * This can make development faster and easier by eliminating the need
 * to define certain beans included in the auto-configuration classes.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
public class CustomFlowableConfiguration {

    /**
     * Class method
     * @return an Engine configuration
     */
    @Bean
    public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> customProcessEngineConfiguration() {
        return processEngineConfiguration -> {
            List<BpmnParseHandler> parseHandlers = processEngineConfiguration.getPostBpmnParseHandlers();
            if (parseHandlers == null) {
                parseHandlers = new ArrayList<>();
            }
            parseHandlers.add(new PagoNxtBpmnUserTaskParseHandler());
            processEngineConfiguration.setPostBpmnParseHandlers(parseHandlers);
        };
    }
}
