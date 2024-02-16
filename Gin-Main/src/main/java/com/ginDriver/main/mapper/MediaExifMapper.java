package com.ginDriver.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ginDriver.main.domain.po.MediaExif;
import com.ginDriver.main.domain.vo.MediaExifVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 媒体资源信息 映射层。
 *
 * @author DueGin
 * @since 1.0
 */
@Mapper
public interface MediaExifMapper extends BaseMapper<MediaExif> {

    List<Map<String, Integer>> selectGroupByMonth(@Param("userId") Long userId);

    List<String> selectGroupByYear(@Param("userId") Long userId);

    List<Map<String, Object>> selectGroupByCityAdCode(@Param("userId") Long userId);

    List<Map<String, Object>> selectGroupByProvinceAdCode(@Param("userId") Long userId);

    /**
     * 通过省份adcode查询exif信息
     * @param adcode
     * @return
     */
    List<MediaExifVO> selectByProvinceAdCode(@Param("adcode") Integer adcode);


    MediaExifVO selectByAdCode(@Param("adcode") Integer adcode);
}
