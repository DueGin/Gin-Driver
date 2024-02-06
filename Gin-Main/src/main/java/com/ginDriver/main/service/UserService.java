package com.ginDriver.main.service;

import com.ginDriver.core.domain.bo.UserBO;
import com.ginDriver.core.domain.po.User;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.exception.ApiException;
import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.cache.redis.UserRedis;
import com.ginDriver.main.domain.vo.*;
import com.ginDriver.main.mapper.RoleMapper;
import com.ginDriver.main.mapper.UserMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author DueGin
 */
@Slf4j
@Service
public class UserService extends MyServiceImpl<UserMapper, User> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private FileService fileService;

    @Resource
    private UserRedis userRedis;

    public User getUserByAccount(String userAccount) {
        return lambdaQuery().eq(User::getUserAccount, userAccount).one();
    }

    public void saveUser(User user) {
        user.setUserAccount(user.getUsername());
        // 插入用户表
        userMapper.insert(user);
        // 插入用户角色表，并给他一个默认角色（普通用户）
        userMapper.insertUserRole(user.getId(), 2L);
    }

    public ResultVO<PageVO<List<SysUserVO>>> queryPage(User user, Integer pageNum, Integer pageSize) {
        log.info(String.valueOf(user));
        Integer count = userMapper.count(user);

        List<User> list = userMapper.page(user, pageSize * (pageNum - 1), pageSize);
        List<Long> userIdList = list.stream().map(User::getId).collect(Collectors.toList());
        List<RoleVO> roleVOList = roleMapper.selectWithUserRoleByUserId(userIdList);
        Map<Long, RoleVO> sysRoleMap = roleVOList.stream().collect(Collectors.toMap(RoleVO::getUserId, r -> r));

        List<GroupRoleVO> groupRoleVOList = roleMapper.selectWithGroupUserRoleByUserId(userIdList);
        Map<Long, List<GroupRoleVO>> groupRoleListMap = groupRoleVOList.stream().collect(Collectors.groupingBy(GroupRoleVO::getUserId));


        List<SysUserVO> vos = list.stream().map(u -> {
            Long userId = u.getId();
            SysUserVO vo = new SysUserVO();
            BeanUtils.copyProperties(u, vo);
            // 系统角色
            vo.setSysRole(sysRoleMap.get(userId).getRoleName());

            // 组角色
            List<GroupRoleVO> groupRoleVOS = groupRoleListMap.get(userId);
            if (groupRoleVOS != null) {
                List<String> groupRoleList = groupRoleVOS.stream()
                        .map(r -> r.getGroupId() + "=" + r.getRoleName())
                        .collect(Collectors.toList());
                vo.setGroupRoleList(groupRoleList);
            } else {
                vo.setGroupRoleList(Collections.emptyList());
            }

            return vo;
        }).collect(Collectors.toList());

        PageVO<List<SysUserVO>> pageVO = new PageVO<>();
        pageVO.setTotal(count);
        pageVO.setPage(pageNum);
        pageVO.setRows(vos);
        return ResultVO.ok(pageVO);
    }

    public ResultVO<Void> deleteUser(Long userId, boolean isAdmin) {

        if (isAdmin) {
            // 判断删的是不是自己
            Long uid = SecurityUtils.getUserId();
            if (uid.equals(userId)) {
                return ResultVO.fail("歪，别删自己啊");
            }
        }

        super.removeById(userId);
        return ResultVO.ok("删除成功！");
    }

    public ResultVO<Void> updateUserInfo(User user, boolean isAdmin) {
        boolean isSelf = false;

        // 判断删的是不是自己
        Long userId = SecurityUtils.getUserId();
        if (user.getId().equals(userId)) {
            isSelf = true;
        }

        if (!isAdmin && !isSelf) {
            throw new ApiException("不能修改别人的信息，除非你是管理员");
        }

        // 更新当前登录用户信息
        boolean modified = updateById(user);
        String msg;
        if (modified) {
            msg = "修改成功！";
            User u = getById(user.getId());
            if (isSelf) {
                UserBO bo = SecurityUtils.getLoginUser();
                if (bo == null) {
                    bo = new UserBO();
                }
                BeanUtils.copyProperties(u, bo);
                userRedis.setUserBO(bo);
            } else {
                // 管理员修改别人的信息
                UserBO bo = new UserBO();
                BeanUtils.copyProperties(u, bo);
                userRedis.setUserBO(bo);
            }
        } else {
            msg = "修改失败！";
        }

        return ResultVO.ok(msg);
    }

    public ResultVO<FileVO> uploadAvatar(MultipartFile file) {
        UserBO bo = SecurityUtils.getLoginUser();
        if (bo == null) {
            throw new ApiException("登录用户异常");
        }

        ResultVO<FileVO> vo = fileService.upload(FileService.FileType.system, file);
        if (StringUtils.isNotBlank(bo.getAvatar())) {
            Boolean deleted = fileService.deleteFile(FileService.FileType.system, bo.getAvatar());
            if (!deleted) {
                log.error("userId: {}, avatar: {} ==> 文件删除失败！", bo.getId(), bo.getAvatar());
            }
        }

        User user = new User();
        user.setId(bo.getId());
        String avatarName = vo.getData().getFileName();
        user.setAvatar(avatarName);
        updateById(user);
        bo.setAvatar(avatarName);
        userRedis.removeUserBO(bo.getId());

        return vo;
    }


}
