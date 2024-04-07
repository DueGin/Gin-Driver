package com.ginDriver.main.service.amap;

import com.ginDriver.main.constant.FileType;
import com.ginDriver.main.constant.MinioConstant;
import com.ginDriver.main.domain.vo.amap.AMapVO;
import com.ginDriver.main.mapper.AMapMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.service.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 高德地图
 *
 * @author DueGin
 */
@Slf4j
@Service
public class AMapService {

    @Resource
    private AMapMapper aMapMapper;

    @Resource
    private MinioService minioService;


    public List<AMapVO> list() {
        List<AMapVO> voList = aMapMapper.selectAllByUserId(SecurityUtils.getUserId());

        voList.forEach(a -> {
            String objUrl = minioService.getObjUrl(FileType.media, a.getObjectName(), MinioConstant.EXPIRE);
            a.setUrl(objUrl);
            a.setObjectName(null);
        });

        return voList;
    }

}
