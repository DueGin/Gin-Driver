package com.ginDriver.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ginDriver.main.domain.po.File;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;


/**
 * @author DueGin
 */
public interface FileMapper extends BaseMapper<File> {

    boolean updateDeletedByIds(@Param("id") Collection<Long> ids, @Param("deleted") int deleted);

    void removeById(@Param("id") long id);

    File getById(@Param("id") long id);
}




