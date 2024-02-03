package com.ginDriver.main.controller.sys;

import com.ginDriver.core.domain.po.User;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.log.GinLog;
import com.ginDriver.main.domain.dto.user.UserQueryParam;
import com.ginDriver.main.domain.vo.PageVO;
import com.ginDriver.main.domain.vo.SysUserVO;
import com.ginDriver.main.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author DueGin
 */
@Api(tags = "用户管理")
@Slf4j
@Validated
@RestController
@RequestMapping("sys/user")
public class SysUserController {

    @Resource
    private UserService userService;

    @ApiOperation("新增系统用户")
    @PostMapping("add")
    public ResultVO<Void> addUser(@RequestBody User user) {
        log.info(String.valueOf(user));
        return userService.addUser(user);
    }

    @ApiOperation("逻辑分页查询")
    @GetMapping("page")
    public ResultVO<PageVO<List<SysUserVO>>> queryUserPage(UserQueryParam userQueryParam) {
        log.info(String.valueOf(userQueryParam));
        return userService.queryPage(userQueryParam, userQueryParam.getPageNum(), userQueryParam.getPageSize());
    }

    @GinLog
    @ApiOperation("修改用户信息")
    @PutMapping("update")
    public ResultVO<Void> updateUser(@RequestBody @Valid User user) {
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
