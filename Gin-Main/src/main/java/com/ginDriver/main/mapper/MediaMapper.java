package com.ginDriver.main.mapper;


import com.ginDriver.main.domain.po.Media;
import com.mybatisflex.core.BaseMapper;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
* @author DueGin
*/
public interface MediaMapper extends BaseMapper<Media> {

//    int insertOrUpdateByMe(Media media);

    Boolean deleteByMediaId(@NotNull Long id);

    /**
     * 根据用户ID查询
     * @param userId 用户ID
     * @return 该用户下的媒体资源
     */
    List<Media> selectByUserId(@NotNull Long userId);

    List<Map<String, Integer>> selectGroupByMonth();

    List<String> selectGroupByYear();

    List<Media> selectBetweenDate(LocalDate startDate, LocalDate endDate);
}




