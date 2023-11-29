package duegin.ginDriver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class GinDriverApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(new Date(System.currentTimeMillis() + 604800L * 1000));
    }

    @Test
    void testUserMapper() {

    }
}
