package com.ginDriver.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.core.domain.bo.UserBO;
import com.ginDriver.core.domain.po.User;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.exception.ApiException;
import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.cache.redis.UserRedis;
import com.ginDriver.main.constant.FileType;
import com.ginDriver.main.domain.dto.user.SysUserPageDTO;
import com.ginDriver.main.domain.dto.user.UpdateUserDTO;
import com.ginDriver.main.domain.vo.FileVO;
import com.ginDriver.main.domain.vo.SysUserVO;
import com.ginDriver.main.mapper.UserMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author DueGin
 */
@Slf4j
@Service
public class UserService extends MyServiceImpl<UserMapper, User> {

    @Resource
    private UserMapper userMapper;

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

    public ResultVO<Page<SysUserVO>> queryPage(SysUserPageDTO page) {
        User user = new User();
        BeanUtils.copyProperties(page, user);
        userMapper.page(page, user);

        return ResultVO.ok(page);
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

    @Transactional
    public ResultVO<Void> updateUserInfo(UpdateUserDTO user, boolean isAdmin) {
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

        if(user.getSysRoleId() != null){
            userMapper.updateUserRole(user.getId(), user.getSysRoleId());
        }

        userRedis.removeUserBO(user.getId());
        return ResultVO.ok(msg);
    }

    public ResultVO<FileVO> uploadAvatar(MultipartFile file) {
        UserBO bo = SecurityUtils.getLoginUser();
        if (bo == null) {
            throw new ApiException("登录用户异常");
        }

        ResultVO<FileVO> vo = fileService.upload(FileType.system, file);
        if (StringUtils.isNotBlank(bo.getAvatar())) {
            Boolean deleted = fileService.deleteFile(FileType.system, bo.getAvatar());
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
