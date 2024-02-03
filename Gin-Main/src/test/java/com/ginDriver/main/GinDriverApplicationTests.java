package com.ginDriver.main;

import cn.hutool.core.map.MapUtil;
import com.alibaba.excel.EasyExcel;
import com.ginDriver.common.utils.http.HttpRequestUtil;
import com.ginDriver.main.domain.dto.place.PlaceDTO;
import com.ginDriver.main.domain.po.Group;
import com.ginDriver.main.domain.po.Place;
import com.ginDriver.main.listener.PlaceListener;
import com.ginDriver.main.service.GroupService;
import com.ginDriver.main.service.PlaceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
    private PlaceService placeService;

    /**
     * 导入地区数据库
     *
     * @throws IOException
     */
    @Test
    void loadAreaData() throws IOException {
        Path path = Paths.get("src/main/resources/data/place/AMap_adcode_citycode.xlsx");
        EasyExcel.read(Files.newInputStream(path), PlaceDTO.class, new PlaceListener())
                .sheet(0)
                .doRead();

        List<PlaceDTO> list = PlaceListener.list;
//        System.out.println(list);

        // <省adcode, <市adcode, [..市所属区域]>>
        Map<Integer, Map<Integer, List<PlaceDTO>>> areaMap = new HashMap<>();
        // <省adcode, 省>
        Map<Integer, PlaceDTO> provinceMap = new HashMap<>();


        List<PlaceDTO> dtos = new ArrayList<>();
        Map<Integer, List<PlaceDTO>> provinceAreaMap = new HashMap<>();
        dtos.add(list.get(0));
        int lastProvinceAdcode = list.get(0).getAdcode();
        int lastCityAdcode = list.get(0).getAdcode();
        int lastCitycode = -1;
        for (PlaceDTO dto : list) {
            if (dto.getCitycode() == null) {
                dto.setProvinceAdcode(dto.getAdcode());
                dto.setCityAdcode(dto.getAdcode());
                provinceMap.put(dto.getAdcode(), dto);
                areaMap.put(lastProvinceAdcode, provinceAreaMap);
                lastProvinceAdcode = dto.getAdcode();
                dtos = new ArrayList<>();
                provinceAreaMap = new HashMap<>();
            } else {
                dto.setProvinceAdcode(lastProvinceAdcode);
                // 新的市
                if (lastCitycode == -1 || dto.getCitycode() != lastCitycode) {
                    lastCitycode = dto.getCitycode();
                    lastCityAdcode = dto.getAdcode();
                    dto.setCityAdcode(lastCityAdcode);
                    dtos = new ArrayList<>();
                    dtos.add(dto);
                    provinceAreaMap.put(dto.getAdcode(), dtos);
                } else {
                    dto.setCityAdcode(lastCityAdcode);
                    dtos.add(dto);
                }
            }
        }

        // 处理剩余的
        if (!provinceAreaMap.isEmpty()) {
            areaMap.put(lastProvinceAdcode, provinceAreaMap);
        }

//        System.out.println(areaMap);
//        System.out.println(provinceMap);

        List<Place> places = new ArrayList<>();
        areaMap.forEach((k1, v1) -> {
            PlaceDTO placeDTO = provinceMap.get(k1);
            Place place = new Place();
            BeanUtils.copyProperties(placeDTO, place);
            places.add(place);
            v1.forEach((k2, v2) -> {
                for (PlaceDTO dto : v2) {
                    Place p = new Place();
                    BeanUtils.copyProperties(dto, p);
                    places.add(p);
                }
            });
        });

        System.out.println(places.size());

        placeService.saveBatch(places);


    }
}
