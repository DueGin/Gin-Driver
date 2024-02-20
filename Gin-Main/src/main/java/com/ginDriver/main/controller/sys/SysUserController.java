package com.ginDriver.main.controller.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.core.domain.po.User;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.log.GinLog;
import com.ginDriver.core.result.BusinessController;
import com.ginDriver.main.domain.dto.user.SysUserPageDTO;
import com.ginDriver.main.domain.dto.user.UpdateUserDTO;
import com.ginDriver.main.domain.vo.SysUserVO;
import com.ginDriver.main.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author DueGin
 */
@Api(tags = "用户管理")
@Slf4j
@Validated
@BusinessController
@RequestMapping("sys/user")
@PreAuthorize("hasRole('ADMIN')")
public class SysUserController {

    @Resource
    private UserService userService;

    @ApiOperation("新增系统用户")
    @PostMapping("add")
    public void addUser(@RequestBody User user) {
        log.info(String.valueOf(user));
        userService.saveUser(user);
    }

    @ApiOperation("逻辑分页查询")
    @GetMapping("page")
    public ResultVO<Page<SysUserVO>> queryUserPage(SysUserPageDTO pageDTO) {
        log.info(String.valueOf(pageDTO));
        return userService.queryPage(pageDTO);
    }

    @GinLog
    @ApiOperation("修改用户信息")
    @PutMapping("update")
    public ResultVO<Void> updateUser(@RequestBody @Valid UpdateUserDTO user) {
        return userService.updateUserInfo(user, true);
    }

    @GinLog
    @ApiOperation("删除用户")
    @DeleteMapping("remove/{userId}")
    public ResultVO<Void> deleteUser(@PathVariable Long userId) {
        log.info(String.valueOf(userId));
        return userService.deleteUser(userId, true);
    }


}
