package duegin.ginDriver.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author DueGin
 */
@Data
@ApiModel("组信息暂时")
public class GroupVO {
    /**
     * 组ID
     */
    @ApiModelProperty("组ID")
    private Long groupId;

    /**
     * 组名
     */
    @ApiModelProperty("组名称")
    private String groupName;

    /**
     * 拥有者用户ID
     */
    @ApiModelProperty("组拥有者ID")
    private Long groupGodId;

    /**
     * 拥有者用户名
     */
    @ApiModelProperty("组拥有者用户名")
    private String groupGodUsername;

    /**
     * 组角色
     */
    @ApiModelProperty("组角色")
    private String roleName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
