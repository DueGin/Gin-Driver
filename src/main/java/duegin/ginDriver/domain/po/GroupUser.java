package duegin.ginDriver.domain.po;

import duegin.ginDriver.domain.dto.group.UpdateGroupUserDTO;
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

    public GroupUser(UpdateGroupUserDTO groupUserParam){
        this.groupId = groupUserParam.getGroupId();
        this.userId = groupUserParam.getUserId();
        this.groupUsername = groupUserParam.getGroupUsername();
    }
}
