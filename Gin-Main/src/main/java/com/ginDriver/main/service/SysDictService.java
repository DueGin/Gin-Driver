package com.ginDriver.main.service;


import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.po.SysDict;
import com.ginDriver.main.mapper.SysDictMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典表 服务层实现。
 *
 * @author DueGin
 * @since 1.0
 */
@Service
public class SysDictService extends MyServiceImpl<SysDictMapper, SysDict> {

    public List<SysDict> getDictByDictType(String dictType) {
        return super.list(QueryWrapper.create().eq(SysDict::getDictType, dictType));
    }

    public Map<Long, SysDict> getDictIdMapByDictType(String dictType) {
        return list(QueryWrapper.create().eq(SysDict::getDictType, dictType))
                .stream()
                .collect(Collectors.toMap(SysDict::getId, d -> d));
    }

    public Map<Integer, SysDict> getDictValueMapByDictType(String dictType) {
        return list(QueryWrapper.create().eq(SysDict::getDictType, dictType))
                .stream()
                .collect(Collectors.toMap(SysDict::getValue, d -> d));
    }
}