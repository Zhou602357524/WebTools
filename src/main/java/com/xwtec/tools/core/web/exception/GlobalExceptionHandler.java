package com.xwtec.tools.core.web.exception;

import com.xwtec.tools.core.entity.ArgumentInvalidResult;
import com.xwtec.tools.core.entity.ResultMsg;
import com.xwtec.tools.core.entity.ResultStatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
//如果返回的为json数据或其它对象，添加该注解
@ResponseBody
public class GlobalExceptionHandler {
    //添加全局异常处理流程，根据需要设置需要处理的异常，本文以MethodArgumentNotValidException为例
    @ExceptionHandler(value=Exception.class)
    public Object MethodArgumentNotValidHandler(HttpServletRequest request, Exception exception) throws Exception {
        //按需重新封装需要返回的错误信息
        List<ArgumentInvalidResult> invalidArguments = new ArrayList<>();
        //解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
        /*for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            ArgumentInvalidResult invalidArgument = new ArgumentInvalidResult();
            invalidArgument.setDefaultMessage(error.getDefaultMessage());
            invalidArgument.setField(error.getField());
            invalidArgument.setRejectedValue(error.getRejectedValue());
            invalidArguments.add(invalidArgument);
        }*/
        ArgumentInvalidResult argumentInvalidResult = new ArgumentInvalidResult();
        argumentInvalidResult.setDefaultMessage(exception.getMessage());

        ResultMsg resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg(), invalidArguments);
        return resultMsg;
    }
}