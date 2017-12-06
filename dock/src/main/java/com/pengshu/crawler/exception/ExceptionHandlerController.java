package com.pengshu.crawler.exception;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ps on 2017/8/1.
 */
@RestController
public class ExceptionHandlerController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = "/error")
    public Object error(HttpServletRequest req , HttpServletResponse resp) {
        // 错误处理逻辑
        return ReturnData.processErr("请求处理异常",null);
    }

    @RequestMapping(value = "/{error}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public Object error(@PathVariable("error") String error) {
        HttpStatus httpStatus=HttpStatus.valueOf(Integer.valueOf(error).intValue());
        return ReturnData.processErr(httpStatus.getReasonPhrase(),error);
    }
}
