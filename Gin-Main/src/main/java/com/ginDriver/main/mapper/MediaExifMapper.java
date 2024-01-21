package com.ginDriver.main.mapper;

import com.ginDriver.main.domain.po.MediaExif;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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

}
