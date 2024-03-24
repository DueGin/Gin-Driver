package com.ginDriver.main.cache.local;

import com.ginDriver.main.domain.po.SysEnum;
import com.ginDriver.main.service.SysEnumService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author DueGin
 */
@Slf4j
public class SysEnumCache {
    private static SysEnumService enumService;

    /**
     * (枚举类型ID, 枚举值Obj)
     */
    private static Map<Integer, List<SysEnum>> ENUM_TYPE_MAP;

    private static Map<Integer, SysEnum> ENUM_MAP;

    public static void init(SysEnumService enumService) {
        SysEnumCache.enumService = enumService;
        loadData();
    }

    public static void loadData() {
        List<SysEnum> list = enumService.list();
        if (list == null || list.isEmpty()) {
            return;
        }

        ENUM_MAP = list.stream().collect(Collectors.toMap(SysEnum::getId, s -> s));

        Map<Integer, List<SysEnum>> map = list.stream().collect(Collectors.groupingBy(SysEnum::getSysEnumTypeId));
        if (!map.isEmpty()) {
            ENUM_TYPE_MAP = map;
        }
    }


    public static List<SysEnum> getEnumListByType(Integer enumTypeId) {
        return ENUM_TYPE_MAP.get(enumTypeId);
    }

    public static SysEnum getEnumById(Integer enumId) {
        return ENUM_MAP.get(enumId);
    }

}
