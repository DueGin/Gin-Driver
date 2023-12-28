package duegin.ginDriver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author DueGin
 */
@SpringBootApplication
@MapperScan({"duegin.ginDriver.mapper"})
@ComponentScan({"com.ginDriver.core","com.ginDriver.common", "duegin.ginDriver"})
public class GinDriverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GinDriverApplication.class, args);
    }

}
