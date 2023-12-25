package duegin.ginDriver;

import duegin.ginDriver.domain.po.Group;
import duegin.ginDriver.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@SpringBootTest
class GinDriverApplicationTests {

    @Resource
    private GroupService groupService;

    @Test
    void contextLoads() {
        System.out.println(new Date(System.currentTimeMillis() + 604800L * 1000));
    }

    @Test
    void testUserMapper() {
        List<Group> list = groupService.list();
        System.out.println(list);
    }
}
