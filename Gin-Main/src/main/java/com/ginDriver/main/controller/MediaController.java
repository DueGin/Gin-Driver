package com.ginDriver.main.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.exception.ApiException;
import com.ginDriver.core.log.GinLog;
import com.ginDriver.core.result.BusinessController;
import com.ginDriver.main.domain.dto.exif.ExifInfoDTO;
import com.ginDriver.main.domain.dto.media.MediaPageDTO;
import com.ginDriver.main.domain.po.Media;
import com.ginDriver.main.domain.vo.FileVO;
import com.ginDriver.main.domain.vo.MediaVO;
import com.ginDriver.main.properties.FileProperties;
import com.ginDriver.main.service.MediaService;
import com.ginDriver.main.service.manager.MediaManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * 媒体资源 控制层。
 *
 * @author DueGin
 * @since 1.0
 */
@Slf4j
@BusinessController
@RequestMapping("/media")
@Tag(name = "媒体资源控制层")
public class MediaController {

    @Resource
    private MediaService mediaService;

    @Resource
    private MediaManager mediaManager;

    @Value("${test.upload_zip_path:/Users/duegin/Desktop/hh/p1.zip}")
    private String uploadZipPath;


    @Resource
    private FileProperties fileProperties;

    @GinLog(isTiming = true)
    @PostMapping("upload")
    public ResultVO<FileVO> upload(ExifInfoDTO exifInfoDTO) {
        try {
            return ResultVO.ok(mediaService.save(exifInfoDTO.getFile(), exifInfoDTO));
        } catch (ApiException e) {
            return ResultVO.fail(e.getMessage());
        }
    }

    @GinLog
    @PostMapping("upload_zip")
    public ResultVO<Void> uploadZip(ExifInfoDTO exifInfoDTO) throws IOException {
        // todo upload zip
        MultipartFile file = exifInfoDTO.getFile();
        file.transferTo(new File(uploadZipPath));
        return ResultVO.ok();
    }

    @GinLog
    @GetMapping("download/{fileName}")
    public void getFile(@PathVariable String fileName, HttpServletResponse resp) {

        Media media = mediaService.lambdaQuery().eq(Media::getFileName, fileName).one();

        try {

            FileInputStream inputStream = new FileInputStream(fileProperties.getFilePrefixPath() + fileName);
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);

            resp.setContentType(media.getContentType());
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            System.out.println("data.length " + data.length);
            resp.setContentLength(data.length);
            resp.setHeader("Content-Range", String.valueOf(data.length - 1));
            resp.setHeader("Accept-Ranges", "bytes");
            OutputStream os = resp.getOutputStream();

            os.write(data);

            os.flush();
            os.close();
            inputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 根据主键删除媒体资源
     *
     * @param ids 主键
     */
    @PostMapping("/remove")
    @Operation(summary = "根据主键删除媒体资源")
    public void remove(@RequestBody Long[] ids) {
        mediaService.delete(ids);
    }


    /**
     * 根据主键更新媒体资源
     *
     * @param media 媒体资源
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    @Operation(summary = "根据主键更新媒体资源")
    public ResultVO<Boolean> update(@RequestBody @Validated(Update.class) Media media) {
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
    public ResultVO<Page<MediaVO>> page(MediaPageDTO page) {
        // 返回图片资源
        return ResultVO.ok(mediaService.getMediaPage(page));
    }

    @GetMapping("thatYearToday/{limit}")
    @Operation(summary = "那年今天")
    public List<MediaVO> thatYearToday(@PathVariable Integer limit) {
        return mediaManager.getThatYearTodayList(limit);
    }

}