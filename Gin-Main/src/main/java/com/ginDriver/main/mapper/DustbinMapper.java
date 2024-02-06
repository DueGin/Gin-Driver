package com.ginDriver.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.main.domain.po.Dustbin;
import com.ginDriver.main.domain.vo.DustbinVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 媒体资源垃圾箱 映射层。
 *
 * @author DueGin
 * @since 1.0
 */
@Mapper
public interface DustbinMapper extends BaseMapper<Dustbin> {

    Page<DustbinVO> getDustbinPage(Page<DustbinVO> page, @Param("userId") Long userId);

}
