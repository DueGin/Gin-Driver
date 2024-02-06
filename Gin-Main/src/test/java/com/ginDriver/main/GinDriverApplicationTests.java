package com.ginDriver.main;

import cn.hutool.core.map.MapUtil;
import com.ginDriver.common.utils.http.HttpRequestUtil;
import com.ginDriver.main.domain.po.Group;
import com.ginDriver.main.domain.vo.MediaVO;
import com.ginDriver.main.mapper.MediaMapper;
import com.ginDriver.main.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

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

    @Test
    void testHttpClient() {
        Map<String, String> map = MapUtil.<String, String>builder()
                .put("output", "json")
                .put("location", "113.319646,23.088111")
                .put("key", "f298653b18f53dfccb4c99f9cddb4afe")
                .build();

        String s = HttpRequestUtil.sendGet("https://restapi.amap.com/v3/geocode/regeo", map, null);
        System.out.println(s);
    }


    @Resource(name = "serviceExecutor")
    private Executor serviceExecutor;

    @Test
    void testExecutor() {
        serviceExecutor.execute(() -> {
            System.out.println("success");
        });
    }

    @Resource
    private MediaMapper mediaMapper;

    @Test
    void testThatYearToday() {
        List<MediaVO> vos = mediaMapper.selectThatYearTodayByUserId(1L, 8, 7, 2);
        System.out.println(vos);
    }
}
