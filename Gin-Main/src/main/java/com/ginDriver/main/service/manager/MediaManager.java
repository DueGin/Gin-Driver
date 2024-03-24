package com.ginDriver.main.service.manager;

import com.ginDriver.main.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author DueGin
 */
@Slf4j
@Service
public class MediaManager {

    @Resource
    private MediaService mediaService;

}
