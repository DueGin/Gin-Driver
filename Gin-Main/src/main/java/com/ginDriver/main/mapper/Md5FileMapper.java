package com.ginDriver.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ginDriver.main.domain.po.Md5File;


/**
 * @author DueGin
 */
public interface Md5FileMapper extends BaseMapper<Md5File> {

    boolean saveOrUpdate(Md5File md5File);
}




