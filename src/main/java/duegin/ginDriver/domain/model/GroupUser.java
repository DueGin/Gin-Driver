package duegin.ginDriver.domain.model;

import duegin.ginDriver.domain.param.group.UpdateGroupUserParam;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DueGin
 */
@Data
@NoArgsConstructor
public class GroupUser {
    private Long groupId;

    private Long userId;

    private Long roleId;

    private String groupUsername;

    public GroupUser(UpdateGroupUserParam groupUserParam){
        this.groupId = groupUserParam.getGroupId();
        this.userId = groupUserParam.getUserId();
        this.groupUsername = groupUserParam.getGroupUsername();
    }
}
