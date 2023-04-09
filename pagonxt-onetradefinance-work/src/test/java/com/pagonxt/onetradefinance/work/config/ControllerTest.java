package com.pagonxt.onetradefinance.work.config;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;


@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "controller-test"})
@WebMvcTest
@Import(ControllerTestConfiguration.class)
public @interface ControllerTest {

    @AliasFor(annotation = WebMvcTest.class, attribute = "value")
    Class<?> value();
}
