package com.ginDriver.core.service.impl;

import com.ginDriver.core.service.MyService;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * @author DueGin
 */
public class MyServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements MyService<T> {
}
