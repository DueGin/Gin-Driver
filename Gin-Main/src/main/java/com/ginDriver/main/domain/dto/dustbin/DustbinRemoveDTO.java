package com.ginDriver.main.domain.dto.dustbin;

import lombok.Data;

import java.util.List;

@Data
public class DustbinRemoveDTO {
    private List<Long> ids;

    /**
     * 文件类型(0：其他) todo 代码枚举类自己定规则，反正扩展的话都是要写代码的
     */
    private Integer fileType;
}
