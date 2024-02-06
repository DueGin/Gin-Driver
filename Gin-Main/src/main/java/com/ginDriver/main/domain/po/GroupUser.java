package com.ginDriver.main.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ginDriver.main.domain.dto.group.UpdateGroupUserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DueGin
 */
@Data
@NoArgsConstructor
@TableName("group_user_role")
public class GroupUser {
    @TableId(type = IdType.AUTO)
    private Long groupId;

    @TableId(type = IdType.AUTO)
    private Long userId;

    @TableId(type = IdType.AUTO)
    private Long roleId;

    private String groupUsername;

    public GroupUser(UpdateGroupUserDTO groupUserParam){
        this.groupId = groupUserParam.getGroupId();
        this.userId = groupUserParam.getUserId();
        this.groupUsername = groupUserParam.getGroupUsername();
    }
}
