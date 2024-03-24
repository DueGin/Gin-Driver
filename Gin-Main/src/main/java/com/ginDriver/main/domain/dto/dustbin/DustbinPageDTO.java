package com.ginDriver.main.domain.dto.dustbin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.main.domain.vo.DustbinVO;
import lombok.Data;

/**
 * @author DueGin
 */
@Data
public class DustbinPageDTO extends Page<DustbinVO> {
    /**
     * 文件类型(枚举ID)
     */
    private Integer fileTypeId;
}
