package duegin.ginDriver.mapper;


import com.mybatisflex.core.BaseMapper;
import duegin.ginDriver.domain.po.Media;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
* @author DueGin
*/
public interface MediaMapper extends BaseMapper<Media> {

    int insertOrUpdateByMe(Media media);

    Boolean deleteByMediaId(@NotNull Long id);

    /**
     * 根据用户ID查询
     * @param userId 用户ID
     * @return 该用户下的媒体资源
     */
    List<Media> selectByUserId(@NotNull Long userId);
}




