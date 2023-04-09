package com.pagonxt.onetradefinance.external.backend.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class
 * This can make development faster and easier by eliminating the need
 * to define certain beans included in the auto-configuration classes.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
@ComponentScan({"com.pagonxt.onetradefinance.integrations",
                "com.pagonxt.onetradefinance.logger"})
public class ModulesConfiguration {
}
