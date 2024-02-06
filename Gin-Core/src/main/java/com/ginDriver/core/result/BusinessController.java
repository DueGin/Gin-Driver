package com.ginDriver.core.result;

import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * 业务控制层注解<br/>
 * 拥有了{@link RestController}能力，并且加上该注解则默认该类下所有方法的返回值都进行处理
 *
 * @author DueGin
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
public @interface BusinessController {
}
