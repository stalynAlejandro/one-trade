package com.pagonxt.onetradefinance.work;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;

/**
 * main project class
 */
@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class})
public class PagonxtOneTradeFinanceWorkApplication {

    /**
     * main method
     */
    public static void main(String[] args) {
        SpringApplication.run(PagonxtOneTradeFinanceWorkApplication.class);
    }

}
