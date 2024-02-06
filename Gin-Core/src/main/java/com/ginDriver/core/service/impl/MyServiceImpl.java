package com.ginDriver.core.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ginDriver.core.service.MyService;

/**
 * @author DueGin
 */
public class MyServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements MyService<T> {
}
