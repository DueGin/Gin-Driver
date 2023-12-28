package duegin.ginDriver.groupAuth;

import duegin.ginDriver.groupAuth.constants.GroupAuthEnum;

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
