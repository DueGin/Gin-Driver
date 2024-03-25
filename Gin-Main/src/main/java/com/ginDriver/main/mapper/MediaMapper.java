package com.ginDriver.main.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.main.domain.po.Media;
import com.ginDriver.main.domain.vo.MediaVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
* @author DueGin
*/
public interface MediaMapper extends BaseMapper<Media> {

    Page<MediaVO> selectPageBetweenDate(Page<MediaVO> page, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") Long userId);

    Page<MediaVO> selectByCityAdCode(Page<MediaVO> page, @Param("adcode") Integer adcode, @Param("userId") Long userId);

    Page<MediaVO> selectByProvinceAdCode(Page<MediaVO> page, @Param("adcode") Integer adcode, @Param("userId") Long userId);

    List<MediaVO> selectThatYearTodayByUserId(@Param("userId") Long userId, @Param("month") Integer month, @Param("day") Integer day, @Param("limit") Integer limit);

    void updateMediaDeleted(@Param("ids") Collection<Long> ids);


    List<Map<String, Integer>> selectGroupByMonth(@Param("userId") Long userId);

    List<String> selectGroupByYear(@Param("userId") Long userId);

    List<Map<String, Object>> selectGroupByCityAdCode(@Param("userId") Long userId);

    List<Map<String, Object>> selectGroupByProvinceAdCode(@Param("userId") Long userId);

//    /**
//     * 通过省份adcode查询exif信息
//     * @param adcode
//     * @return
//     */
//    List<MediaExifVO> selectByProvinceAdCode(@Param("adcode") Integer adcode);


    Page<MediaVO> selectPageWithFile(Page<MediaVO> page, @Param("userId") Long userId, @Param("isSelf") Boolean isSelf);

    boolean removeByFileIds(Collection<Long> fileIds);
}




