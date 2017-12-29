package com.xwtec.tools.config.exceptionhandler;

import com.xwtec.tools.core.entity.ArgumentInvalidResult;
import com.xwtec.tools.core.entity.ResultMsg;
import com.xwtec.tools.core.entity.ResultStatusCode;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler extends AbstractExceptionHandler {

    @ExceptionHandler(value = BindException.class)
    public Object MethodArgumentNotValidHandler(HttpServletRequest request,
                                                BindException exception) throws Exception {
        printException(exception);
        //按需重新封装需要返回的错误信息
        List<ArgumentInvalidResult> invalidArguments = new ArrayList<>();
        //解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
        exception.getBindingResult().getFieldErrors().forEach(e -> {
            ArgumentInvalidResult invalidArgument = new ArgumentInvalidResult();
            invalidArgument.setDefaultMessage(e.getDefaultMessage());
            invalidArgument.setField(e.getField());
            invalidArgument.setRejectedValue(e.getRejectedValue());
            invalidArguments.add(invalidArgument);
        });
        return new ResultMsg(ResultStatusCode.PARAMETER_ERROR.getErrcode(), ResultStatusCode.PARAMETER_ERROR.getErrmsg(), invalidArguments);
    }

    @ExceptionHandler(Throwable.class)
    public Object GlobalException(HttpServletRequest request, Throwable e) {
        printException(e);
        return new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg(), e.getMessage());
    }

}