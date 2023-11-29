package duegin.ginDriver.domain.model;


import lombok.Data;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 媒体资源
 * @author DueGin
 * @TableName media
 */
@Data
public class Media implements Serializable {
    /**
     * 媒体ID
     */
    @NotNull(groups = Update.class)
    private Long mediaId;

    /**
     * 上传用户
     */
    @NotNull(groups = Insert.class)
    private Long userId;

    /**
     * 文件名
     */
    @NotNull(groups = Insert.class)
    private String fileName;

    /**
     * 文件类型1：图片，2：视频，3：电影，4：其他
     */
    @NotNull(groups = Insert.class)
    private Integer type;

    /**
     * 文件路径
     */
    @NotNull(groups = Insert.class)
    private String src;

    /**
     * 文件状态
     */
    @NotNull(groups = Insert.class)
    private Integer status;

    /**
     * 是否为公众媒体
     */
    @NotNull(groups = Insert.class)
    private Integer isShare;

    /**
     * 媒体格式
     */
    @NotNull(groups = Insert.class)
    private String format;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     *
     */
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}