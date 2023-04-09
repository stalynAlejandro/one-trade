package com.pagonxt.onetradefinance.work.config;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;


@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
public @interface UnitTest {

}
