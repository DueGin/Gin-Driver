package com.ginDriver.main.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.result.BusinessController;
import com.ginDriver.main.domain.dto.sys.dict.SysDictPageDTO;
import com.ginDriver.main.domain.po.SysDict;
import com.ginDriver.main.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典表 控制层。
 *
 * @author DueGin
 * @since 1.0
 */
@Validated
@BusinessController
@RequestMapping("/sys/dict")
@PreAuthorize("hasRole('ADMIN')")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    /**
     * 添加 字典表
     *
     * @param sysDict 字典表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    public ResultVO<Boolean> save(@RequestBody SysDict sysDict) {
        return ResultVO.ok(sysDictService.save(sysDict));
    }


    /**
     * 根据主键删除字典表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    public ResultVO<Boolean> remove(@PathVariable Serializable id) {
        return ResultVO.ok(sysDictService.removeById(id));
    }


    /**
     * 根据主键更新字典表
     *
     * @param sysDict 字典表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    public ResultVO<Boolean> update(@RequestBody SysDict sysDict) {
        return ResultVO.ok(sysDictService.updateById(sysDict));
    }


    /**
     * 查询所有字典表
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    public ResultVO<List<SysDict>> list() {
        return ResultVO.ok(sysDictService.list());
    }


    /**
     * 根据字典表主键获取详细信息。
     *
     * @param id sysDict主键
     * @return 字典表详情
     */
    @GetMapping("/getInfo/{id}")
    public ResultVO<SysDict> getInfo(@PathVariable Serializable id) {
        return ResultVO.ok(sysDictService.getById(id));
    }


    /**
     * 分页查询字典表
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    public ResultVO<Page<SysDict>> page(@Valid SysDictPageDTO page) {
        sysDictService.page(page, new QueryWrapper<SysDict>().lambda()
                .eq(SysDict::getDictType, page.getDictType()));

        return ResultVO.ok(page);
    }

    @GetMapping("/list/{dictType}")
    public ResultVO<List<SysDict>> getDictListByDictType(@PathVariable String dictType) {
        return ResultVO.ok(sysDictService.getDictByDictType(dictType));
    }

    @GetMapping("/map/{dictType}")
    public ResultVO<Map<String, Integer>> getDictMapByDictType(@PathVariable String dictType) {
        List<SysDict> dictList = sysDictService.getDictByDictType(dictType);
        Map<String, Integer> map = new HashMap<>();

        dictList.forEach(sysDict -> map.put(sysDict.getLabel(), sysDict.getValue()));

        return ResultVO.ok(map);
    }
}