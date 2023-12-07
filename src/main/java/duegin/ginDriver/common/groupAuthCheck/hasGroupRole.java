package duegin.ginDriver.common.groupAuthCheck;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author DueGin
 */
@Target({ElementType.METHOD})
public @interface hasGroupRole {
    String value();

    String needRole();
}
