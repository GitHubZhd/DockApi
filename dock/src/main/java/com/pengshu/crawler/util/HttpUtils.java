package com.pengshu.crawler.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by ps on 2017/7/27.
 */
public class HttpUtils {

    public static final class HttpMethod {
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
        public static final String HEAD = "HEAD";
        public static final String TRACE = "TRACE";
    }

    public static HttpUriRequest httpRequest(HttpServletRequest request ,byte[] entity , String url) throws IOException {

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(180000)
                .setConnectTimeout(180000)
                .setConnectionRequestTimeout(180000)
                .build();

        String method=request.getMethod();
        RequestBuilder requestBuilder = null;

        if(method.equalsIgnoreCase(HttpMethod.GET)){
            requestBuilder=RequestBuilder.get();

        }else if(method.equalsIgnoreCase(HttpMethod.POST)){
            requestBuilder=RequestBuilder.post();

            if(request.getContentType()!=null
                    && request.getContentType().contains(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)){
                requestBuilder.setEntity(new ByteArrayEntity(entity));
            }else {
                Enumeration paramNames = request.getParameterNames();
                while (paramNames.hasMoreElements()) {
                    String paramName = (String) paramNames.nextElement();
                    String paramValue = request.getParameter(paramName);
                    requestBuilder.addParameter(paramName,paramValue);
                }
            }

        }else if(method.equalsIgnoreCase(HttpMethod.PUT)){
            requestBuilder=RequestBuilder.put();

        }else if(method.equalsIgnoreCase(HttpMethod.DELETE)){
            requestBuilder=RequestBuilder.delete();

        }else if(method.equalsIgnoreCase(HttpMethod.HEAD)){
            requestBuilder=RequestBuilder.head();

        }else if(method.equalsIgnoreCase(HttpMethod.TRACE)){
            requestBuilder=RequestBuilder.trace();

        }else {
            throw new IllegalArgumentException("Illegal HTTP Method " + method);
        }
        requestBuilder.setConfig(defaultRequestConfig);
        requestBuilder.setUri(url);
        HttpUriRequest httpUriRequest=requestBuilder.build();

        //设置请求数据报文头信息
        Enumeration<String> headerNames=request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name=headerNames.nextElement();
            String header=request.getHeader(name);
            //当body非空时，会自动加上Content-Length请求头及其对应值，不需要自己手动加上它
            if(!name.equals("content-length")){
                httpUriRequest.setHeader(name,header);
            }
        }

        return httpUriRequest;
    }
}
