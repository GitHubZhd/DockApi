package com.pengshu.crawler.app;

import com.pengshu.crawler.util.HttpUtils;
import com.pengshu.crawler.util.Utils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ps on 2017/7/5.
 * 请求透传
 */
//@RestController
@RequestMapping("/openApi/crawler")
@ConfigurationProperties
public class PassthroughController {

    @Value("${host}")
    private String host;
    private static final Logger logger= LoggerFactory.getLogger(PassthroughController.class);

    /**
     * 处理POST请求
     * @param request
     * @return
     */
    @RequestMapping(value = "/app/**/",produces = MediaType.APPLICATION_JSON_VALUE)
    public  Object Handle(HttpServletRequest request ,HttpServletResponse response) throws IOException{
        byte[] tempResult= Utils.InputStreamToByte(request.getInputStream());

        CloseableHttpClient httpclient = HttpClients.createDefault();
        //新建Http请求
        String url=host+request.getRequestURI().substring(20);

        HttpUriRequest httpUriRequest1=HttpUtils.httpRequest(request,tempResult,url);

        //处理请求，得到响应
        HttpResponse res = httpclient.execute(httpUriRequest1);

        for (Header header:res.getAllHeaders()) {
            String headerName = header.getName();
            if(!headerName.equals("Transfer-Encoding")){
                response.setHeader(headerName,header.getValue());
            }
        }

        byte[] dataByteArr = Utils.InputStreamToByte(res.getEntity().getContent());

        return dataByteArr;
    }

    /**
     * 处理Get请求
     * @param request
     * @return
     */
    @RequestMapping(value = "/app/**/",method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
    public Object HandleGet(HttpServletRequest request , HttpServletResponse response) throws IOException{
        String param = request.getQueryString();
        String uri=request.getRequestURI().substring(20);
        if(param!=null){
            uri=uri+"?"+param;
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpUriRequest httpUriRequest=HttpUtils.httpRequest(request,null,host+uri);

        //处理请求，得到响应
        HttpResponse res = httpclient.execute(httpUriRequest);

        for (Header header:res.getAllHeaders()) {
            String headerName = header.getName();
            if(!headerName.equals("Transfer-Encoding")){
                response.setHeader(headerName,header.getValue());
            }
        }

        byte[] dataByteArr = Utils.InputStreamToByte(res.getEntity().getContent());

        return dataByteArr;
    }

}
