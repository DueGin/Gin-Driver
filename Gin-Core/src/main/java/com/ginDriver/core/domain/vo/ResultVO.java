package com.ginDriver.core.domain.vo;

import com.ginDriver.core.constant.ResultEnum;
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
public class ResultVO<T> {
    @ApiModelProperty("状态码")
    private Integer code;
    @ApiModelProperty("信息")
    private String msg;
    @ApiModelProperty("数据")
    private T data;

    ResultVO(ResultEnum resultEnum){
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public static ResultVO<Void> ok(){
        return new ResultVO<>(ResultEnum.SUCCESS.getCode(), null, null);
    }

    public static ResultVO<Void> ok(String msg){
        return new ResultVO<>(ResultEnum.SUCCESS.getCode(), msg, null);
    }


    public static ResultVO<Void> ok(ResultEnum resultEnum){
        return new ResultVO<>(resultEnum.getCode(), resultEnum.getMsg(), null);
    }

    public static ResultVO<Void> ok(Integer code, String msg){
        return new ResultVO<>(code, msg, null);
    }

    public static <T> ResultVO<T> ok(T data){
        return new ResultVO<>(ResultEnum.SUCCESS.getCode(), null, data);
    }

    public static <T> ResultVO<T> fail(String msg){
        return new ResultVO<>(ResultEnum.ERROR.getCode(), msg, null);
    }
}
