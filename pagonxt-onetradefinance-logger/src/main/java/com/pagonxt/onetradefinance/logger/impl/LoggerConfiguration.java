package com.pagonxt.onetradefinance.logger.impl;
import com.pagonxt.onetradefinance.logger.LoggerWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class LoggerConfiguration {


    @Bean("loggerWrapper")
    @Profile("santander")
    LoggerWrapper loggerWrapper() {
        return new LoggerWrapperImpl();
    }

}
