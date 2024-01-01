package com.ginDriver.main.domain.dto.media;

import com.ginDriver.main.domain.po.Media;
import com.mybatisflex.core.paginate.Page;
import lombok.Data;

/**
 * @author DueGin
 */
@Data
public class MediaDTO extends Page<Media> {
    /**
     * 是否只查看自己的
     */
    private Boolean onlyLookSelf;
}
