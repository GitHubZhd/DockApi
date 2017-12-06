package com.pengshu.crawler.exception;

import com.pengshu.crawler.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ps on 2017/7/19.
 */
@RestControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger= LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public Object defaultErrorHandler(HttpServletRequest request , HttpServletResponse response , Exception ex){

        logger.info(Utils.getStackTrace(ex));

        return ReturnData.processErr(ex.getMessage(),null);
    }

}
