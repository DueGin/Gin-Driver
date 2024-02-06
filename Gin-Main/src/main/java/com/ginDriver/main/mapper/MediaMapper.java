package com.ginDriver.main.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.main.domain.po.Media;
import com.ginDriver.main.domain.vo.MediaVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
* @author DueGin
*/
public interface MediaMapper extends BaseMapper<Media> {

    Page<MediaVO> selectPageBetweenDate(Page<MediaVO> page, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    Page<MediaVO> selectByCityAdCode(Page<MediaVO> page, @Param("adcode") Integer adcode);

    Page<MediaVO> selectByProvinceAdCode(Page<MediaVO> page, @Param("adcode") Integer adcode);

    List<MediaVO> selectThatYearTodayByUserId(@Param("userId") Long userId, @Param("month") Integer month, @Param("day") Integer day, @Param("limit") Integer limit);
}




