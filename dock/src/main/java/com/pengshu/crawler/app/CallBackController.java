package com.pengshu.crawler.app;

import com.pengshu.crawler.exception.ReturnData;
import com.pengshu.crawler.util.HttpUtils;
import com.pengshu.crawler.util.Utils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ps on 2017/7/24.
 * 回调处理
 */
@RestController
public class CallBackController {

    private static final Logger logger= LoggerFactory.getLogger(CallBackController.class);

    @RequestMapping(value = "/callback/**" ,produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    public Object doCallBack(HttpServletRequest request , HttpServletResponse response) throws IOException, JSONException {

        String tempResult= Utils.InputStreamToString(request.getInputStream());
        logger.info(tempResult);

        JSONObject jsStr = new JSONObject(tempResult);
        JSONObject basicInfo = (JSONObject) jsStr.get("basicInfo");
        JSONArray cbUrl= (JSONArray) basicInfo.get("cbUrl");

        if(cbUrl ==null || cbUrl.length()<1){
            throw new RuntimeException("该请求不是回调请求");
        }
        String url= (String) cbUrl.get(0);
        logger.info(url);

        //新建Http请求
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpUriRequest httpUriRequest=HttpUtils.httpRequest(request,Utils.paraMessage(tempResult).getBytes(),url);

        //处理请求，得到响应
        HttpResponse res = httpclient.execute(httpUriRequest);

        for (Header header:res.getAllHeaders()) {
            String headerName = header.getName();
            if(!headerName.equals("Transfer-Encoding")){
                response.setHeader(headerName,header.getValue());
            }
        }

        byte[] dataByteArr = Utils.InputStreamToByte(res.getEntity().getContent());

        String returnData= ReturnData.process(new String(dataByteArr));
        logger.info("响应数据："+returnData);

        return returnData;

    }
}
