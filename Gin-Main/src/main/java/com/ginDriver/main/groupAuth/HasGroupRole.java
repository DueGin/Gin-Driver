package com.ginDriver.main.groupAuth;

import com.ginDriver.main.groupAuth.constants.GroupAuthEnum;

import java.lang.annotation.*;

/**
 * @author DueGin
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HasGroupRole {
    GroupAuthEnum value();
}
