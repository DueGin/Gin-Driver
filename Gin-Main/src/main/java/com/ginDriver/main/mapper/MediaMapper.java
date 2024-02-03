package com.ginDriver.main.mapper;


import com.ginDriver.main.domain.po.Media;
import com.ginDriver.main.domain.vo.MediaVO;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
* @author DueGin
*/
public interface MediaMapper extends BaseMapper<Media> {

    List<MediaVO> selectBetweenDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<MediaVO> selectByCityAdCode(@Param("adcode") Integer adcode);

    List<MediaVO> selectByProvinceAdCode(@Param("adcode") Integer adcode);
}




