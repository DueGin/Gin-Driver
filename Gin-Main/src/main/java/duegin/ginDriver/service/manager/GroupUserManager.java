package duegin.ginDriver.service.manager;

import duegin.ginDriver.domain.po.Group;
import duegin.ginDriver.mapper.GroupMapper;
import duegin.ginDriver.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author DueGin
 * 组用户管理
 */
@Service
@Slf4j
public class GroupUserManager {

    @Resource
    private GroupMapper groupMapper;

    @Transactional
    public Boolean createGroup(Group group) {
        groupMapper.save(group);
        // 给组普通用户
        groupMapper.insertGroupUserRole(group.getId(), group.getUserId(), 5L, SecurityUtils.getLoginUser().getUsername());
        return true;
    }

}
