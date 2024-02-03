package com.ginDriver.main.controller;

import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.log.GinLog;
import com.ginDriver.main.domain.dto.exif.ExifInfoDTO;
import com.ginDriver.main.domain.dto.media.MediaDTO;
import com.ginDriver.main.domain.po.Media;
import com.ginDriver.main.domain.vo.FileVO;
import com.ginDriver.main.domain.vo.MediaVO;
import com.ginDriver.main.service.MediaService;
import com.mybatisflex.core.paginate.Page;
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

//    /**
//     * 添加 媒体资源
//     *
//     * @param media 媒体资源
//     * @return {@code true} 添加成功，{@code false} 添加失败
//     */
//    @PostMapping("/save")
//    @Operation(summary = "添加媒体资源")
//    public ResultVO<Void> save(@RequestBody Media media) {
//        log.info(String.valueOf(media));
//        MediaMapper mapper = (MediaMapper) mediaService.getMapper();
//        mapper.insertOrUpdateByMe(media);
//        return ResultVO.ok();
//    }

    @GinLog(isTiming = true)
    @PostMapping("upload")
    public ResultVO<FileVO> upload(ExifInfoDTO exifInfoDTO) {
        return ResultVO.ok(mediaService.save(exifInfoDTO.getFile(), exifInfoDTO));
    }

    @PostMapping("upload_zip")
    public ResultVO<Void> uploadZip() {
        // todo
        return ResultVO.ok();
    }

    /**
     * 根据主键删除媒体资源
     *
     * @param ids 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PostMapping("/remove")
    @Operation(summary = "根据主键删除媒体资源")
    public ResultVO<Void> remove(@RequestBody Long[] ids) {
        return mediaService.delete(ids);
    }


    /**
     * 根据主键更新媒体资源
     *
     * @param media 媒体资源
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    @Operation(summary = "根据主键更新媒体资源")
    public ResultVO<Boolean> update(@RequestBody Media media) {
        return ResultVO.ok(mediaService.updateById(media));
    }


    /**
     * 查询所有媒体资源
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有媒体资源")
    public ResultVO<List<Media>> list() {
        return ResultVO.ok(mediaService.list());
    }


    /**
     * 根据媒体资源主键获取详细信息。
     *
     * @param id media主键
     * @return 媒体资源详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据媒体资源主键获取详细信息")
    @Parameter(name = "id", description = "", required = true)
    public ResultVO<Media> getInfo(@PathVariable Serializable id) {
        return ResultVO.ok(mediaService.getById(id));
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
    public ResultVO<Page<MediaVO>> page(MediaDTO page) {
        // 返回图片资源
        return ResultVO.ok(mediaService.getMediaPage(page));
    }


}