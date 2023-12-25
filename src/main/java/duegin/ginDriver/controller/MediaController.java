package duegin.ginDriver.controller;

import com.mybatisflex.core.paginate.Page;
import duegin.ginDriver.domain.po.Media;
import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.mapper.MediaMapper;
import duegin.ginDriver.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 媒体资源 控制层。
 *
 * @author DueGin
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/media")
@Tag(name = "媒体资源控制层")
public class MediaController {

    @Resource
    private MediaService mediaService;

    /**
     * 添加 媒体资源
     *
     * @param media 媒体资源
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加媒体资源")
    public Result<Void> save(@RequestBody Media media) {
        log.info(String.valueOf(media));
        MediaMapper mapper = (MediaMapper) mediaService.getMapper();
        mapper.insertOrUpdateByMe(media);
        return Result.ok();
    }


    /**
     * 根据主键删除媒体资源
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    @Operation(summary = "根据主键删除媒体资源")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true)
    })
    public Result<Boolean> remove(@PathVariable Serializable id) {
        return Result.ok(mediaService.removeById(id));
    }


    /**
     * 根据主键更新媒体资源
     *
     * @param media 媒体资源
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    @Operation(summary = "根据主键更新媒体资源")
    public Result<Boolean> update(@RequestBody Media media) {
        return Result.ok(mediaService.updateById(media));
    }


    /**
     * 查询所有媒体资源
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有媒体资源")
    public Result<List<Media>> list() {
        return Result.ok(mediaService.list());
    }


    /**
     * 根据媒体资源主键获取详细信息。
     *
     * @param id media主键
     * @return 媒体资源详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据媒体资源主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true)
    })
    public Result<Media> getInfo(@PathVariable Serializable id) {
        return Result.ok(mediaService.getById(id));
    }

    /**
     * 分页查询媒体资源
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询媒体资源")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public Result<Page<Media>> page(Page<Media> page) {
        // 返回图片资源
        return Result.ok(mediaService.page(page));
    }
}