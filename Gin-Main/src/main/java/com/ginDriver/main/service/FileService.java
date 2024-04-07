package com.ginDriver.main.service;

import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.po.File;
import com.ginDriver.main.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author DueGin
 */
@Service
@Slf4j
public class FileService extends MyServiceImpl<FileMapper, File> {
}
