package duegin.ginDriver.controller.sys;

import com.mybatisflex.core.paginate.Page;
import duegin.ginDriver.domain.po.SysDict;
import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RestController
@RequestMapping("/sysDict")
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
    public Result<Boolean> save(@RequestBody SysDict sysDict) {
        return Result.ok(sysDictService.save(sysDict));
    }


    /**
     * 根据主键删除字典表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    public Result<Boolean> remove(@PathVariable Serializable id) {
        return Result.ok(sysDictService.removeById(id));
    }


    /**
     * 根据主键更新字典表
     *
     * @param sysDict 字典表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody SysDict sysDict) {
        return Result.ok(sysDictService.updateById(sysDict));
    }


    /**
     * 查询所有字典表
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    public Result<List<SysDict>> list() {
        return Result.ok(sysDictService.list());
    }


    /**
     * 根据字典表主键获取详细信息。
     *
     * @param id sysDict主键
     * @return 字典表详情
     */
    @GetMapping("/getInfo/{id}")
    public Result<SysDict> getInfo(@PathVariable Serializable id) {
        return Result.ok(sysDictService.getById(id));
    }


    /**
     * 分页查询字典表
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    public Result<Page<SysDict>> page(Page<SysDict> page) {
        return Result.ok(sysDictService.page(page));
    }

    @GetMapping("/list/{dictType}")
    public Result<List<SysDict>> getDictListByDictType(@PathVariable String dictType) {
        return Result.ok(sysDictService.getDictByDictType(dictType));
    }

    @GetMapping("/map/{dictType}")
    public Result<Map<String, Integer>> getDictMapByDictType(@PathVariable String dictType) {
        List<SysDict> dictList = sysDictService.getDictByDictType(dictType);
        Map<String, Integer> map = new HashMap<>();

        dictList.forEach(sysDict-> map.put(sysDict.getLabel(), sysDict.getValue()));

        return Result.ok(map);
    }
}