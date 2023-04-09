package com.pagonxt.onetradefinance.work;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.fail;

@RunWith(SpringRunner.class)
class PagonxtOneTradeFinanceWorkApplicationTest {

    @Test
    void testMain() {
        try{
            PagonxtOneTradeFinanceWorkApplication.main(new String[] {});
        }
        catch (Exception exception){
            fail("A exception has occurred: " + exception);
        }
    }
}