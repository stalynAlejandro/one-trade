package com.pagonxt.onetradefinance.logger.mock;
import com.pagonxt.onetradefinance.logger.LoggerWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockLoggerConfiguration {

    @Bean("loggerWrapper")
    @ConditionalOnMissingBean
    LoggerWrapper mockLoggerWrapper() {
        return new MockLoggerWrapperImpl();
    }

}
