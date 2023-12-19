package duegin.ginDriver.domain.po;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组
 * @author DueGin
 * @TableName group
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Group implements Serializable {
    /**
     * 组ID
     */
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 组名
     */
    @NotNull(groups = Insert.class)
    private String groupName;

    /**
     * 创建者用户ID
     */
    @NotNull(groups = Insert.class)
    private Long userId;

    /**
     * 简介
     */
    private String description;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 软删除
     */
    private Integer deleted;

    private static final long serialVersionUID = 1L;

}