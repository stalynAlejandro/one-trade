package com.pagonxt.onetradefinance.control;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class PagonxtOneTradeFinanceControlApplicationTest {

    @Autowired
    private ApplicationContext appContext;

    @Test
    void contextLoads() {
        assertThat(appContext).isNotNull();
    }

    @Test
    void testMain() {
        try{
            PagonxtOneTradeFinanceControlApplication.main(new String[] {});
        }
        catch (Exception exception){
            fail("A exception has occurred: " + exception.toString());
        }
    }
}
