package duegin.ginDriver.core.service.impl;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import duegin.ginDriver.core.service.MyService;

/**
 * @author DueGin
 */
public class MyServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements MyService<T> {
}
