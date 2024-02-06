package com.ginDriver.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ginDriver.main.domain.po.SysDict;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典表 映射层。
 *
 * @author DueGin
 * @since 1.0
 */
@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {


}
