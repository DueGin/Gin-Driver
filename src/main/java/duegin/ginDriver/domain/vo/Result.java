package duegin.ginDriver.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author DueGin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result <T> {
    private Integer code;
    private String msg;
    private T data;

    public static Result<Void> ok(){
        return new Result<>(200, null, null);
    }

    public static <T> Result<T> ok(T data){
        return new Result<>(200, null, data);
    }

    public static Result<Void> fail(String msg){
        return new Result<>(500, msg, null);
    }
}
