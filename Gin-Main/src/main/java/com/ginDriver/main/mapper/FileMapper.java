package com.ginDriver.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ginDriver.main.domain.po.File;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;


/**
 * @author DueGin
 */
public interface FileMapper extends BaseMapper<File> {

    boolean updateDeletedByIds(@Param("ids") Collection<Long> ids, @Param("deleted") int deleted);

    void removeById(@Param("id") long id);

    boolean removeByIds(@Param("ids") Collection<Long> ids);

    File getById(@Param("id") long id);

    List<File> selectAllByIds(@Param("ids") Collection<Long> ids);
}




