package duegin.ginDriver.domain.po;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * 菜单权限表 实体类。
 *
 * @author DueGin
 * @since 1.0
 */
@Data
@Table(value = "menu")
public class Menu {

    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 菜单名称
     */
    @Column(value = "name")
    private String name;

    /**
     * 父菜单ID
     */
    @Column(value = "parent_id")
    private Long parentId;

    /**
     * 路由地址
     */
    @Column(value = "path")
    private String path;

    /**
     * 组件路径
     */
    @Column(value = "component")
    private String component;

    /**
     * 菜单图标
     */
    @Column(value = "icon")
    private String icon;

    /**
     * 权限标识
     */
    @Column(value = "perms")
    private String perms;

    /**
     * 菜单状态（1正常 0停用）
     */
    @Column(value = "status")
    private Integer status;

    /**
     * 菜单类型(0:启动台菜单，1:组资源菜单，2:媒体管理菜单，3:其他)
     */
    @Column(value = "type")
    private Integer type;

    /**
     * 排序
     */
    @Column("sorted")
    private Integer sorted;

    /**
     * 创建时间
     */
    @Column(value = "create_time")
    private java.time.LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(value = "update_time")
    private java.time.LocalDateTime updateTime;

    /**
     * 备注
     */
    @Column(value = "remark")
    private String remark;


}
