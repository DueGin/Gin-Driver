package com.ginDriver.main.domain.po;

import com.ginDriver.main.domain.dto.group.UpdateGroupUserDTO;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DueGin
 */
@Data
@NoArgsConstructor
@Table("group_user_role")
public class GroupUser {
    @Id
    private Long groupId;

    @Id
    private Long userId;

    @Id
    private Long roleId;

    private String groupUsername;

    public GroupUser(UpdateGroupUserDTO groupUserParam){
        this.groupId = groupUserParam.getGroupId();
        this.userId = groupUserParam.getUserId();
        this.groupUsername = groupUserParam.getGroupUsername();
    }
}
