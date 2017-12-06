package com.pengshu.crawler.serviceImpl;

import com.pengshu.crawler.service.HandleService;
import com.pengshu.crawler.util.HttpUtils;
import com.pengshu.crawler.util.Utils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ps on 2017/8/11.
 */
@Service
public class HandleServiceImpl implements HandleService {

    private Logger logger= LoggerFactory.getLogger(HandleServiceImpl.class);

    @Override
    public Object getHandle(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        String param = request.getQueryString();
        if(param!=null){
            url=url+"?"+param;
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpUriRequest httpUriRequest= HttpUtils.httpRequest(request,null,url);

        //处理请求，得到响应
        HttpResponse res = httpclient.execute(httpUriRequest);

        for (Header header:res.getAllHeaders()) {
            String headerName = header.getName();
            if(!headerName.equals("Transfer-Encoding")){
                response.setHeader(headerName,header.getValue());
            }
        }
        response.setStatus(res.getStatusLine().getStatusCode());

        byte[] dataByteArr = null;
        try{
            dataByteArr = EntityUtils.toByteArray(res.getEntity());
        }catch (Exception e){
            e.printStackTrace();
        }

        //logger.info("响应报文："+ByteArrayUtil.toHexString(dataByteArr));
        return dataByteArr;
    }

    @Override
    public Object otherHandle(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {

        byte[] temp=Utils.InputStreamToByte(request.getInputStream());

        CloseableHttpClient httpclient = HttpClients.createDefault();

        //新建Http请求
        HttpUriRequest httpUriRequest1=HttpUtils.httpRequest(request,temp,url);

        //处理请求，得到响应
        HttpResponse res = httpclient.execute(httpUriRequest1);

        for (Header header:res.getAllHeaders()) {
            String headerName = header.getName();
            if(!headerName.equals("Transfer-Encoding")){
                response.setHeader(headerName,header.getValue());
            }
        }
        response.setStatus(res.getStatusLine().getStatusCode());

        byte[] dataByteArr = null;
        try{
            dataByteArr = EntityUtils.toByteArray(res.getEntity());
        }catch (Exception e){
            e.printStackTrace();
        }

        //logger.info("响应报文："+ByteArrayUtil.toHexString(dataByteArr));

        return dataByteArr;
    }
}
