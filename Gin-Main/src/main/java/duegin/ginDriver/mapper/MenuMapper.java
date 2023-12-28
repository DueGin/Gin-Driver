package duegin.ginDriver.mapper;

import com.mybatisflex.core.BaseMapper;
import duegin.ginDriver.domain.po.Menu;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单权限表 映射层。
 *
 * @author DueGin
 * @since 1.0
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {


}
