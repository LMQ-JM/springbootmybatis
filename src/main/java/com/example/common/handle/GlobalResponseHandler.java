package com.example.common.handle;


import com.example.common.anntation.IgnoreResponseAdvice;
import com.example.common.data.Result;
import com.example.common.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;

/**
 * 全局统一返回
 * @Author MQ
 * @Date 2019/8/31
 * @Version 1.0
 */
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice {
    static final Logger logger = LoggerFactory.getLogger(GlobalResponseHandler.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        if (methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class) ||
                aClass.isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }

        /**
         * 配置swagger
         */
        if (methodParameter.getMember().getName().equals("error") || methodParameter.getMember().getName().equals("uiConfiguration")
              || methodParameter.getMember().getName().equals("securityConfiguration") || methodParameter.getMember().getName().equals("swaggerResources")
              || methodParameter.getMember().getName().equals("getDocumentation")) {
            return false;
        }
        return true;
    }

    @Override
    public Result beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof ApplicationException) {
            ApplicationException exception = (ApplicationException) o;
            Result result = new Result();
            result.setCode(exception.getCode());
            result.setMsg(exception.getMsg());

            return result;
        }

        return Result.SUCCESS(o);
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView uploadException(MaxUploadSizeExceededException e) throws IOException {
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg", "最大上传文件为30M，上传文件大小超出限制!");
        mv.setViewName("error");
        return mv;
    }


}
