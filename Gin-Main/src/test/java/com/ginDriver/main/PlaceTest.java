package com.ginDriver.main;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ginDriver.main.domain.dto.place.PlaceDTO;
import com.ginDriver.main.domain.po.Place;
import com.ginDriver.main.listener.PlaceListener;
import com.ginDriver.main.service.PlaceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DueGin
 */
@SpringBootTest
public class PlaceTest {
    @Resource
    private PlaceService placeService;

    /**
     * 导入地区数据库
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

        placeService.remove(new QueryWrapper<>());
        long l = System.currentTimeMillis();
        // 假批量
        placeService.saveBatch(places, 100);

//        placeService.getBaseMapper().insertBatchSomeColumn(places);

        System.out.println(System.currentTimeMillis() - l);
    }
}
