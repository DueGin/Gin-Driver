package duegin.ginDriver.controller.sys;

import com.ginDriver.core.domain.po.User;
import com.ginDriver.core.domain.vo.ResultVO;
import duegin.ginDriver.domain.dto.user.UserQueryParam;
import duegin.ginDriver.domain.vo.PageVO;
import duegin.ginDriver.domain.vo.SysUserVO;
import duegin.ginDriver.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @ApiOperation("修改系统用户信息")
    @PutMapping("modify")
    public ResultVO<Void> modifyUser(@RequestBody User user) {
        log.info(String.valueOf(user));
        return userService.modifyUserInfo(user);
    }

    @ApiOperation("删除系统用户")
    @DeleteMapping("{userId}")
    public ResultVO<Void> deleteUser(@PathVariable Long userId) {
        log.info(String.valueOf(userId));
        return userService.deleteUser(userId);
    }


}
