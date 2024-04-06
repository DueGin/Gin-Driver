package com.ginDriver.main.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.po.Md5File;
import com.ginDriver.main.mapper.Md5FileMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author DueGin
 */
@Service
public class Md5FileService extends MyServiceImpl<Md5FileMapper, Md5File> {

    @Transactional
    public boolean subRef(Long md5FileId, Integer subCount) {
        if (md5FileId == null || subCount == null) {
            return false;
        }
        return super.lambdaUpdate().setSql("ref = ref - " + subCount).eq(Md5File::getId, md5FileId).update();
    }

    public boolean removeByMd5AndContentType(String md5, String contentType){
        QueryWrapper<Md5File> qw = new QueryWrapper<>();
        qw.lambda().eq(Md5File::getMd5, md5).eq(Md5File::getContentType, contentType);
        return super.remove(qw);
    }

    public List<Md5File> getMd5FileRef0List(){
        return super.lambdaQuery().eq(Md5File::getRef, 0).list();
    }
}




