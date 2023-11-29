package duegin.ginDriver.mapper;


import duegin.ginDriver.domain.model.Media;
import org.apache.ibatis.annotations.Insert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
* @author DueGin
*/
public interface MediaMapper {

    Boolean insertOrUpdate(@Validated(Insert.class) Media media);

    Boolean deleteByMediaId(@NotNull Long mediaId);

    /**
     * 根据用户ID查询
     * @param userId 用户ID
     * @return 该用户下的媒体资源
     */
    List<Media> selectByUserId(@NotNull Long userId);
}




