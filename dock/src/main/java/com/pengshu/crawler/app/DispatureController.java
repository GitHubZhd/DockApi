package com.pengshu.crawler.app;

import com.pengshu.crawler.exception.ReturnData;
import com.pengshu.crawler.service.HandleService;
import com.pengshu.crawler.util.HttpUtils;
import com.pengshu.crawler.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ps on 2017/8/11.
 */
@RestController
@RequestMapping("/openApi/**")
public class DispatureController {

    private static final Logger logger= LoggerFactory.getLogger(DispatureController.class);

    @Autowired
    private HandleService handleService;

//    @Resource
//    private AsyncService asyncService;

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Object doService(HttpServletRequest request , HttpServletResponse response){

        String host= (String) request.getAttribute("host");
        String uri= (String) request.getAttribute("uri");
        if(StringUtils.isEmpty(host) || StringUtils.isEmpty(uri)){
            throw new RuntimeException("请求url异常");
        }

        String method=request.getMethod();

        try {
            //异步请求
//            HttpUriRequest httpUriRequest=asyncService.isAsyn(request,host+uri);
//            if(httpUriRequest!=null){
//                asyncService.asynHandle(httpUriRequest);
//                JSONObject message=new JSONObject();
//                message.put("message","我已收到请求");
//                return ReturnData.process(message.toString());
//            }

            if(HttpUtils.HttpMethod.GET.equalsIgnoreCase(method)){
                return handleService.getHandle(request,response,host+uri);
            }else{
                return handleService.otherHandle(request,response,host+uri);
            }

        } catch (IOException e) {
            logger.info(Utils.getStackTrace(e));
            return ReturnData.processErr("请求异常","200003");
        }

    }
}
