package com.ginDriver.main.service;


import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.po.LayoutComponent;
import com.ginDriver.main.mapper.LayoutComponentMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 组件布局 服务层实现。
 *
 * @author DueGin
 * @since 1.0
 */
@Service
public class LayoutComponentService extends MyServiceImpl<LayoutComponentMapper, LayoutComponent> {

    public Map<Long, LayoutComponent> getLayoutComponentMap(Collection<Long> ids){
        return super.list(QueryWrapper.create()
                        .from(LayoutComponent.class)
                        .in(LayoutComponent::getId, ids, !CollectionUtils.isEmpty(ids)))
                .stream()
                .collect(Collectors.toMap(LayoutComponent::getId, l -> l));
    }

}