package duegin.ginDriver.domain.vo;

import duegin.ginDriver.constant.ResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author DueGin
 */
@ApiModel("统一结果返回对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result <T> {
    @ApiModelProperty("状态码")
    private Integer code;
    @ApiModelProperty("信息")
    private String msg;
    @ApiModelProperty("数据")
    private T data;

    Result(ResultEnum resultEnum){
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public static Result<Void> ok(){
        return new Result<>(ResultEnum.SUCCESS.getCode(), null, null);
    }

    public static Result<Void> ok(String msg){
        return new Result<>(ResultEnum.SUCCESS.getCode(), msg, null);
    }


    public static Result<Void> ok(ResultEnum resultEnum){
        return new Result<>(resultEnum.getCode(), resultEnum.getMsg(), null);
    }

    public static Result<Void> ok(Integer code, String msg){
        return new Result<>(code, msg, null);
    }

    public static <T> Result<T> ok(T data){
        return new Result<>(ResultEnum.SUCCESS.getCode(), null, data);
    }

    public static Result<Void> fail(String msg){
        return new Result<>(ResultEnum.ERROR.getCode(), msg, null);
    }
}
