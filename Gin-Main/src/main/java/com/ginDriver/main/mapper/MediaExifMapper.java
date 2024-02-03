package com.ginDriver.main.mapper;

import com.ginDriver.main.domain.po.MediaExif;
import com.ginDriver.main.domain.vo.MediaExifVO;
import com.mybatisflex.core.BaseMapper;
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

    List<Map<String, Integer>> selectGroupByMonth();

    List<String> selectGroupByYear();

    List<Map<String, Object>> selectGroupByCityAdCode();

    List<Map<String, Object>> selectGroupByProvinceAdCode();

    /**
     * 通过省份adcode查询exif信息
     * @param adcode
     * @return
     */
    List<MediaExifVO> selectByProvinceAdCode(@Param("adcode") Integer adcode);


    MediaExifVO selectByAdCode(@Param("adcode") Integer adcode);
}
