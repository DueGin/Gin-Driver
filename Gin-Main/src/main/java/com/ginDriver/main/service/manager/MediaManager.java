package com.ginDriver.main.service.manager;

import com.ginDriver.core.exception.ApiException;
import com.ginDriver.main.domain.vo.MediaVO;
import com.ginDriver.main.mapper.MediaMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @author DueGin
 */
@Slf4j
@Service
public class MediaManager {

    @Resource
    private MediaService mediaService;

    public List<MediaVO> getThatYearTodayList(Integer limit) {
        MediaMapper mediaMapper = mediaService.getBaseMapper();
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new ApiException("你登录了吗？");
        }

        LocalDate today = LocalDate.now();
        List<MediaVO> vos = mediaMapper.selectThatYearTodayByUserId(userId, today.getMonthValue(), today.getDayOfMonth(), limit);

        mediaService.setMinioUrl(vos);

        return vos;
    }
}
