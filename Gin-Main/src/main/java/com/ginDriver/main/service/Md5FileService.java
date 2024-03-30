package com.ginDriver.main.service;


import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.po.Md5File;
import com.ginDriver.main.mapper.Md5FileMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DueGin
 */
@Service
public class Md5FileService extends MyServiceImpl<Md5FileMapper, Md5File> {

    @Transactional
    public boolean subRef(String md5, Integer subCount){
        return super.lambdaUpdate().setSql("ref = ref - " + subCount).eq(Md5File::getMd5, md5).update();
    }
}




