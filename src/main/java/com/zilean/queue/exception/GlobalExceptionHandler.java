package com.zilean.queue.exception;

import com.zilean.queue.domain.response.ZileanResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @ Author     ：XJH.
 * @ Date       ：Created in 16:24 2017/11/30/030.
 * @ Description：全局异常处理类
 * @ Modified By：
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 400 - Bad Request
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ZileanResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new ZileanResponse<>(-400, "Required String parameter '" + e.getParameterName() + "' is not present");
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ZileanResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return new ZileanResponse<>(-405, "Request method " + e.getMethod() + " not supported");
    }

    /**
     * 空指针异常处理
     *
     * @param request request
     * @return Map
     */
    @ExceptionHandler(NullPointerException.class)
    public ZileanResponse nullPointerException(HttpServletRequest request) {
        log.error("空指针异常 ：uri: {}.", request.getRequestURI());
        return new ZileanResponse<>(ZileanExceptionEnum.ERROR_SYSTEM_BUSY);
    }


    /**
     * 业务异常
     *
     * @param e e
     * @return ZileanResponse
     */
    @ExceptionHandler(ZileanException.class)
    public ZileanResponse bizException(ZileanException e) {
        log.error("[error]{}.", e.getBizExceptionEnum().toString());
        return new ZileanResponse<>(e.getBizExceptionEnum());
    }


    /**
     * create by    :XJH
     * description  :Spring MVC 对象参数校验异常
     * create time  :2018/3/11/030
     *
     * @param e e
     * @return com.gysdlmy.euroluce.dto.BaseResult
     */
    @ExceptionHandler(BindException.class)
    public ZileanResponse exception(BindException e) {
        FieldError fieldError = e.getFieldError();

        String code = fieldError.getCode();                                     //错误类型
        String field = fieldError.getField();                                   //错误的字段
        String defaultMessage = fieldError.getDefaultMessage();                 //错误的信息

        String errorMessage = field + " 错误";
        if (Objects.equals(code, "Size")) {
            //长度错误
            errorMessage = field + " " + defaultMessage;
        } else if (Objects.equals(code, "typeMismatch")) {
            //类型错误
            errorMessage = field + " 类型不匹配";
        } else if (Objects.equals(code, "NotNull")) {
            //不为空错误
            errorMessage = field + defaultMessage;
        }
        return new ZileanResponse<>(-400, errorMessage, null);
    }

    /**
     * 整体异常
     *
     * @param request request
     * @param e       e
     * @return ZileanResponse
     */
    @ExceptionHandler(Exception.class)
    public ZileanResponse exception(HttpServletRequest request, Exception e) {
        log.error("[error][exception]:uri:{},{}.", request.getRequestURI(), e);
        return new ZileanResponse<>(ZileanExceptionEnum.ERROR_SYSTEM_BUSY);
    }
}
