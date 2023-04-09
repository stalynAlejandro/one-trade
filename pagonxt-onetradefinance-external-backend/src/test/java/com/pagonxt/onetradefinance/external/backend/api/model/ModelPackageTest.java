package com.pagonxt.onetradefinance.external.backend.api.model;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoClassFilter;
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
    private static final String PACKAGE_NAME = "com.pagonxt.onetradefinance.external.backend.api.model";

    @Test
    void validate() {
        List<String> filteredOutClasses = List.of("Filter");
        List<PojoClass> pojoClasses = PojoClassFactory.getPojoClassesRecursively(PACKAGE_NAME, new FilterOutClassName(filteredOutClasses));

        Validator validator = ValidatorBuilder.create()
                .with(new SetterMustExistRule(),
                        new GetterMustExistRule())
                .with(new SetterTester(),
                        new GetterTester())
                .build();

        pojoClasses.forEach(validator::validate);
    }

    public static class FilterOutClassName implements PojoClassFilter {

        private final List<String> classNameList;

        public List<String> getClassNameList() {
            return classNameList;
        }

        public FilterOutClassName(List<String> classNameList) {
            this.classNameList = classNameList;

        }

        public boolean include(PojoClass pojoClass) {
            for(String className : getClassNameList()) {
                if(pojoClass.getName().endsWith(className)) {
                    return false;
                }
            }
            return true;
        }

        public boolean equals(Object o) {
            return this == o || o != null && this.getClass() == o.getClass();
        }

        public int hashCode() {
            return this.getClass().hashCode();
        }
    }
}
