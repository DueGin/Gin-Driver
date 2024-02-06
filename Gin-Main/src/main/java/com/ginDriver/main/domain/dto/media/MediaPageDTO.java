package com.ginDriver.main.domain.dto.media;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.main.domain.po.Media;
import lombok.Data;

/**
 * @author DueGin
 */
@Data
public class MediaPageDTO extends Page<Media> {
    /**
     * 是否只查看自己的
     */
    private Boolean onlyLookSelf;
}
