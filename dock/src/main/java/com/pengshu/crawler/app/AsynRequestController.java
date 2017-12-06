package com.pengshu.crawler.app;

import com.pengshu.crawler.exception.ReturnData;
import com.pengshu.crawler.service.AsyncService;
import com.pengshu.crawler.util.HttpUtils;
import com.pengshu.crawler.util.Utils;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ps on 2017/7/14.
 * 异步请求处理
 */
@RestController
@ConfigurationProperties
public class AsynRequestController {

    private final static Logger logger= LoggerFactory.getLogger(AsynRequestController.class);

    @Resource
    private AsyncService asyncService;

    @Value("${host}")
    private String host;

    @RequestMapping(value = "/asyn/openApi/**",method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
    public Object doService(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, JSONException, IOException {

        String tempResult= Utils.InputStreamToString(request.getInputStream());

        JSONArray cbUrl= Utils.paraCbUrl(tempResult);
        JSONArray newCbUrl=new JSONArray();
        for (int i = 0; i <cbUrl.length() ; i++) {
            newCbUrl.put(i+1,cbUrl.get(i));
        }
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("http://").append(Utils.getLocalIP()).append(":8080/callback/call");
        newCbUrl.put(0,stringBuffer.toString());

        String uri=host+request.getRequestURI().substring(13);
        HttpUriRequest httpUriRequest= HttpUtils.httpRequest(request,Utils.paraMessage(tempResult).getBytes(),uri);
        httpUriRequest.setHeader("Url",newCbUrl.toString());
        /**
         * 调用异步请求
         */
        asyncService.asynHandle(httpUriRequest);
        logger.info("我已收到请求");

        JSONObject message=new JSONObject();
        message.put("message","我已收到请求");
        return ReturnData.process(message.toString());

    }

}
