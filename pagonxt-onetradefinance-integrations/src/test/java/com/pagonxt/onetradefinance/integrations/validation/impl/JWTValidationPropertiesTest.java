package com.pagonxt.onetradefinance.integrations.validation.impl;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class JWTValidationPropertiesTest {

    @Test
    void testGettersAndSetters() {
        Validator pojoValidator =
                ValidatorBuilder.create()
                        .with(new SetterMustExistRule(), new GetterMustExistRule())
                        .with(new SetterTester(), new GetterTester())
                        .build();

        pojoValidator.validate(PojoClassFactory.getPojoClass(JWTValidationProperties.class));
    }
}
