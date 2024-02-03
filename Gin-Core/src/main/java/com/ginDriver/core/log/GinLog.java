package com.ginDriver.core.log;

import java.lang.annotation.*;

/**
 * @author DueGin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GinLog {
    String value() default "";

    String afterMethodMsg() default "";

    String thrExceptionMsg() default "";

    boolean isTiming() default false;
}
