package com.ginDriver.main.controller.aMap;

import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.main.domain.vo.amap.AMapVO;
import com.ginDriver.main.service.amap.AMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 高德地图
 * @author DueGin
 */
@Slf4j
@RestController
@RequestMapping("amap")
public class AMapController {

    @Resource
    private AMapService aMapService;

    @GetMapping("list")
    public ResultVO<List<AMapVO>> list() {
        return ResultVO.ok(aMapService.list());
    }
}
