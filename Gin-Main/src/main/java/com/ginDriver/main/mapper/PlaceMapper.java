package com.ginDriver.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ginDriver.main.domain.po.Place;
import org.apache.ibatis.annotations.Mapper;

/**
 * 行政区域 映射层。
 *
 * @author DueGin
 * @since 1.0
 */
@Mapper
public interface PlaceMapper extends BaseMapper<Place> {


}
