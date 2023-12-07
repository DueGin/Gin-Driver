package duegin.ginDriver.exception;

import duegin.ginDriver.constant.ResultEnum;
import duegin.ginDriver.domain.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @author DueGin
 */
@Slf4j
@RestControllerAdvice("duegin.ginDriver.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> validatedExceptionHandler(ConstraintViolationException e){
        log.error(String.valueOf(e));
        return Result.ok(ResultEnum.PARAMETER_ERROR.getCode(), e.getMessage());
    }

//    /**
//     * 忽略参数异常处理器
//     *
//     * @param e 忽略参数异常
//     * @return Result
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    public Result<Void> parameterMissingExceptionHandler(MissingServletRequestParameterException e) {
//        log.error(String.valueOf(e));
//        return Result.ok(ResultEnum.PARAMETER_ERROR.getCode(), "请求参数 " + e.getParameterName() + " 不能为空");
//    }
//
//    /**
//     * 缺少请求体异常处理器
//     *
//     * @param e 缺少请求体异常
//     * @return Result
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public Result<Void> parameterBodyMissingExceptionHandler(HttpMessageNotReadableException e) {
//        log.error(String.valueOf(e));
//        return Result.ok(ResultEnum.PARAMETER_ERROR.getCode(), "请求体不能为空");
//    }
//
//    /**
//     * 参数效验异常处理器
//     *
//     * @param e 参数验证异常
//     * @return ResponseInfo
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Result<Void> parameterExceptionHandler(MethodArgumentNotValidException e) {
//        log.error(String.valueOf(e));
//        // 获取异常信息
//        BindingResult exceptions = e.getBindingResult();
//        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
//        if (exceptions.hasErrors()) {
//            List<ObjectError> errors = exceptions.getAllErrors();
//            if (!errors.isEmpty()) {
//                // 这里列出了全部错误参数，按正常逻辑，只需要第一条错误即可
//                FieldError fieldError = (FieldError) errors.get(0);
//                return Result.ok(ResultEnum.PARAMETER_ERROR.getCode(), fieldError.getDefaultMessage());
//            }
//        }
//        return Result.ok(ResultEnum.PARAMETER_ERROR);
//    }

}
