package com.ginDriver.core.result;

import java.lang.annotation.*;

/**
 * controller类上加了{@link BusinessController}注解，想临时取消处理返回值，则可以使用该注解添加在方法上，以忽略处理响应值
 * @author DueGin
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessIgnore {
}
