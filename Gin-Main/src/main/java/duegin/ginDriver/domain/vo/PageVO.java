package duegin.ginDriver.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DueGin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVO<T> {
    private Integer total;

    private Integer page;

    private T rows;
}
