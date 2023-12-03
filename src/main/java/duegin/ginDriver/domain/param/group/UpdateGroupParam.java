package duegin.ginDriver.domain.param.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author DueGin
 */
@Data
@ApiModel("组更新信息")
public class UpdateGroupParam {

    /**
     * 组ID
     */
    @ApiModelProperty(value = "组ID", required = true)
    private Long groupId;

    /**
     * 组名
     */
    @NotNull
    @ApiModelProperty(value = "组名",required = true)
    private String groupName;
}
