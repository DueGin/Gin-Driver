package com.ginDriver.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author DueGin
 */
@SpringBootApplication
@MapperScan({"com.ginDriver.*.mapper"})
@ComponentScan({"com.ginDriver.core","com.ginDriver.common", "com.ginDriver.main"})
public class GinDriverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GinDriverApplication.class, args);
    }

}
