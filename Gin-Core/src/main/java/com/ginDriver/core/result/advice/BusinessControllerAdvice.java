package com.ginDriver.core.result.advice;

import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.result.BusinessController;
import com.ginDriver.core.result.BusinessIgnore;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 处理业务controller的返回类
 *
 * @author DueGin
 */
@RestControllerAdvice(annotations = {BusinessController.class})
public class BusinessControllerAdvice implements ResponseBodyAdvice<Object> {

    public BusinessControllerAdvice() {
    }

    /**
     * 判断是否处理，如果方法头上存在{@link BusinessIgnore}，则返回false(不处理)
     *
     * @param returnType the return type
     * @param aClass     the selected converter type
     * @return {@code true}-执行{@link BusinessControllerAdvice#beforeBodyWrite}处理，{@code false}-不执行
     */
    @Override
    public boolean supports(MethodParameter returnType, @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
        return returnType.getMethod() != null
                && !returnType.getMethod().getReturnType().equals(ResultVO.class)
                && !returnType.getMethod().isAnnotationPresent(BusinessIgnore.class);
    }

    /**
     * 将响应值封装到{@link ResultVO}中
     *
     * @param data       the body to be written
     * @param returnType the return type of the controller method
     * @param mediaType  the content type selected through content negotiation
     * @param aClass     the converter type selected to write to the response
     * @param request    the current request
     * @param response   the current response
     * @return {@link ResultVO}
     */
    @Override
    @Nullable
    public Object beforeBodyWrite(
            Object data,
            @NotNull MethodParameter returnType,
            @NotNull MediaType mediaType,
            @NotNull Class<? extends HttpMessageConverter<?>> aClass,
            @NotNull ServerHttpRequest request,
            @NotNull ServerHttpResponse response
    ) {
        return data != null && data.getClass() == String.class ? ResultVO.ok((String) data) : ResultVO.ok(data);
    }
}
