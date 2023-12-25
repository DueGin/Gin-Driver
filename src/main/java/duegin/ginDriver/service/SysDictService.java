package duegin.ginDriver.service;


import com.mybatisflex.core.query.QueryWrapper;
import duegin.ginDriver.core.service.impl.MyServiceImpl;
import duegin.ginDriver.domain.po.SysDict;
import duegin.ginDriver.mapper.SysDictMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典表 服务层实现。
 *
 * @author DueGin
 * @since 1.0
 */
@Service
public class SysDictService extends MyServiceImpl<SysDictMapper, SysDict> {

    public List<SysDict> getDictByDictType(String dictType){
        return super.list(QueryWrapper.create().eq(SysDict::getDictType, dictType));
    }
}