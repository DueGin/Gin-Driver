package com.ginDriver.main.service;


import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.po.Place;
import com.ginDriver.main.mapper.PlaceMapper;
import org.springframework.stereotype.Service;

/**
 * 行政区域 服务层实现。
 *
 * @author DueGin
 * @since 1.0
 */
@Service
public class PlaceService extends MyServiceImpl<PlaceMapper, Place> {

}