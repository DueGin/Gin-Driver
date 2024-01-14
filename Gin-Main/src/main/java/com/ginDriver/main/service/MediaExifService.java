package com.ginDriver.main.service;


import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.po.MediaExif;
import com.ginDriver.main.mapper.MediaExifMapper;
import org.springframework.stereotype.Service;

/**
 * 媒体资源信息 服务层实现。
 *
 * @author DueGin
 * @since 1.0
 */
@Service
public class MediaExifService extends MyServiceImpl<MediaExifMapper, MediaExif> {

}