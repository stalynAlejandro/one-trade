package com.pagonxt.onetradefinance.integrations.apis.common.security.configuration;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.filters.FilterClassName;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.jupiter.api.Test;

import java.util.List;

class ConfigurationPackageTest {
    private static final String PACKAGE_NAME = "com.pagonxt.onetradefinance.integrations.apis.common.security.configuration";

    @Test
    void validate() {
        List<PojoClass> pojoClasses = PojoClassFactory.getPojoClassesRecursively(PACKAGE_NAME, new FilterClassName("\\w*Properties\\w*$"));

        Validator validator = ValidatorBuilder.create()
                .with(new SetterMustExistRule(),
                        new GetterMustExistRule())
                .with(new SetterTester(),
                        new GetterTester())
                .build();

        pojoClasses.forEach(validator::validate);
    }
}
