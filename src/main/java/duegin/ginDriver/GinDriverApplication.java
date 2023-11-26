package duegin.ginDriver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author DueGin
 */
@SpringBootApplication
@MapperScan({"duegin.ginDriver.mapper"})
public class GinDriverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GinDriverApplication.class, args);
    }

}
