package com.ginDriver.main.controller.sys;

import com.ginDriver.core.domain.bo.UserBO;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.result.BusinessController;
import com.ginDriver.core.result.BusinessIgnore;
import com.ginDriver.main.security.utils.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author DueGin
 */
@RequestMapping("sys/test")
@BusinessController
public class TestController {

    @GetMapping("testString")
    public String testString(){
        return "hhhhh";
    }

    @GetMapping("testObj")
    public UserBO testObj(){
        return SecurityUtils.getLoginUser();
    }

    @GetMapping("testResultVO1")
    public ResultVO testResultVo1(){
        String s="testResultVO";
        return ResultVO.ok(s);
    }

    @GetMapping("testResultVO2")
    public ResultVO testResultVo2(){
        String s="testResultVO";
        return ResultVO.ok((Object) s);
    }

    @BusinessIgnore
    @GetMapping("ignore")
    public UserBO ignore(){
        return SecurityUtils.getLoginUser();
    }

    @GetMapping("void")
    public void vvoid(){
        System.out.println("do void");
    }

}
