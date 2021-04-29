package com.example.common.handle;

import com.example.common.data.Result;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理类
 * @author MQ
 * @date 2021/4/29 11:05
 */
@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(value = FileSizeLimitExceededException.class)
    Result arithmeticExceptionException(FileSizeLimitExceededException e, HttpServletRequest request) {
        return new Result(-1,"上传单个文件大小只能上传10M","");
    }

    @ExceptionHandler(value = Exception.class)
    Result handlerException(Exception e, HttpServletRequest request) {
        return new Result(500,"服务器异常","");
    }
}
