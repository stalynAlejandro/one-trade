package com.pagonxt.onetradefinance.work.configuration;

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

    /**
     * This Configuration file exits to put the component scan for the integrations module
     * If you need further tuning related to the integrations module, you can do it here
     */

}
