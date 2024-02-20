package com.ginDriver.main.domain.dto.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.main.domain.vo.SysUserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author DueGin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class SysUserPageDTO extends Page<SysUserVO> {
    private String username;
}
