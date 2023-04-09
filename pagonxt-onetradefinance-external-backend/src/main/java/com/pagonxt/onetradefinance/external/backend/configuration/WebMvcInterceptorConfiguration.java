package com.pagonxt.onetradefinance.external.backend.configuration;

import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.integrations.validation.JWTValidation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for the interceptor of the JWT token sent by apigee in the header.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
@ComponentScan("com.pagonxt.onetradefinance.integrations.validation")
@Profile("apigee")
public class WebMvcInterceptorConfiguration implements WebMvcConfigurer {

    /**
     * The controller path
     */
    @Value("${controller.path}")
    private String controllerPath;

    /**
     * Flag to mock user as backOffice
     */
    @Value("${santander.token-validation.mock-bo:false}")
    private boolean mockBO;

    /**
     * The JWT validator
     */
    private final JWTValidation jwtValidation;

    /**
     * The office info service
     */
    private final OfficeInfoService officeInfoService;

    /**
     * Constructor.
     *
     * @param jwtValidation     : the JWT validator
     * @param officeInfoService : the office info service
     */
    public WebMvcInterceptorConfiguration(JWTValidation jwtValidation, OfficeInfoService officeInfoService) {
        this.jwtValidation = jwtValidation;
        this.officeInfoService = officeInfoService;
    }

    /**
     * Method to include custom interceptors to all the controller endpoints.
     *
     * @param registry : the interceptor registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(jwtValidation, officeInfoService, mockBO))
                .addPathPatterns(controllerPath + "/**");
    }

}
