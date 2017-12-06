package com.pengshu.crawler.serviceImpl;

import com.pengshu.crawler.service.AsyncService;
import com.pengshu.crawler.util.HttpUtils;
import com.pengshu.crawler.util.Utils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Created by ps on 2017/7/24.
 */
@Service
public class AsyncServiceImpl implements AsyncService {

    private static final Logger logger= LoggerFactory.getLogger(AsyncServiceImpl.class);

    @Async
    public Future<Object> asynHandle(HttpUriRequest httpUriRequest) throws IOException {


        //新建Http请求
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //处理请求，得到响应
        HttpResponse res = httpclient.execute(httpUriRequest);

        logger.info("=======status："+res.getStatusLine().getStatusCode()+"===============");
        return new AsyncResult<Object>(res);
    }

    public HttpUriRequest isAsyn(HttpServletRequest request,String url){
        try {
            if(request.getRequestURI().startsWith("/openApi/crawler/")){
                return null;
            }
            String tempResult= Utils.InputStreamToString(request.getInputStream());

            JSONArray cbUrl= Utils.paraCbUrl(tempResult);
            if(cbUrl==null || cbUrl.length()<1){
                return null;
            }
            JSONArray newCbUrl=new JSONArray();
            for (int i = 0; i <cbUrl.length() ; i++) {
                newCbUrl.put(i+1,cbUrl.get(i));
            }
            StringBuffer stringBuffer=new StringBuffer();
            stringBuffer.append("http://").append(Utils.getLocalIP()).append(":8080/callback/call");
            newCbUrl.put(0,stringBuffer.toString());

            HttpUriRequest httpUriRequest= HttpUtils.httpRequest(request,Utils.paraMessage(tempResult).getBytes(),url);
            httpUriRequest.setHeader("Url",newCbUrl.toString());

            return httpUriRequest;
        }  catch (IOException e) {
            logger.info(Utils.getStackTrace(e));
        } catch (JSONException e) {
            logger.info(Utils.getStackTrace(e));
        }
        return null;
    }

}
