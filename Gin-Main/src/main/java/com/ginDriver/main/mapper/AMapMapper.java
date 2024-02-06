package com.ginDriver.main.mapper;


import com.ginDriver.main.domain.vo.amap.AMapVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 媒体资源垃圾箱 映射层。
 *
 * @author DueGin
 * @since 1.0
 */
public interface AMapMapper {
    List<AMapVO> selectAllByUserId(@Param("userId") Long userId);
}
