package com.ginDriver.main.controller.sys;

import com.ginDriver.core.result.BusinessController;
import com.ginDriver.main.domain.po.SysEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author DueGin
 */
@BusinessController
@Slf4j
@RequestMapping("sys/enum")
public class SysEnumController {
    @GetMapping("list")
    public List<SysEnum> list(){

    }
}
