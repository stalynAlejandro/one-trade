package com.pagonxt.onetradefinance.work.service.model;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.jupiter.api.Test;

import java.util.List;

class ModelPackageTest {
    private static final String PACKAGE_NAME = "com.pagonxt.onetradefinance.work.service.model";

    @Test
    void validate() {
        List<PojoClass> pojoClasses = PojoClassFactory.getPojoClassesRecursively(PACKAGE_NAME, null);

        Validator validator = ValidatorBuilder.create()
                .with(new SetterMustExistRule(),
                        new GetterMustExistRule())
                .with(new SetterTester(),
                        new GetterTester())
                .build();

        pojoClasses.forEach(validator::validate);
    }
}
