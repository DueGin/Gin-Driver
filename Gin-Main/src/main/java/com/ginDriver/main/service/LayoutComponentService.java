package com.ginDriver.main.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.po.LayoutComponent;
import com.ginDriver.main.mapper.LayoutComponentMapper;
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
        return super.list(new QueryWrapper<LayoutComponent>().lambda()
                        .in(!CollectionUtils.isEmpty(ids), LayoutComponent::getId, ids)
                )
                .stream()
                .collect(Collectors.toMap(LayoutComponent::getId, l -> l));
    }

}