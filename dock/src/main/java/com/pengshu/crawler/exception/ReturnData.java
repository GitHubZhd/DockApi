package com.pengshu.crawler.exception;

import com.pengshu.crawler.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ps on 2017/7/21.
 */
public class ReturnData {
    private final static Logger logger= LoggerFactory.getLogger(ReturnData.class);

    public static String process(String message) {
        JSONObject jsonResponse=new JSONObject();
        try {
            //响应编码，详见《对接编码.md》
            jsonResponse.put("code","200");
            //响应消息描述,详见《对接编码.md》
            jsonResponse.put("msg","success");
            //响应时间戳
            jsonResponse.put("rTS",System.currentTimeMillis());
            //错误消息
            JSONObject err=new JSONObject();
            jsonResponse.put("err",err);
            //响应结果内容
            JSONObject msg=new JSONObject(message);
            jsonResponse.put("response",msg);
            //下步操作
            JSONObject next=new JSONObject();
            jsonResponse.put("next",next);
            //报文签名信息，Json格式，机构开启签名验证时才有此项
            JSONObject secInfo=new JSONObject();
            secInfo.put("sign","");
            secInfo.put("signTyp","");
            jsonResponse.put("secInfo",secInfo);
        } catch (JSONException e) {
            logger.info(Utils.getStackTrace(e));
            throw new RuntimeException("openApi返回报文异常");
        }
        return jsonResponse.toString();
    }

    public static String processErr(String errMsg,String code) {
        JSONObject jsonResponse=new JSONObject();
        try {
            //响应编码，详见《对接编码.md》
            jsonResponse.put("code",code);
            //响应消息描述,详见《对接编码.md》
            jsonResponse.put("msg",errMsg);
            //响应时间戳
            jsonResponse.put("rTS",System.currentTimeMillis());
            //错误消息
            JSONObject err=new JSONObject();
            jsonResponse.put("err",err);
            //响应结果内容
            JSONObject msg=new JSONObject();
            jsonResponse.put("response",msg);
            //下步操作
            JSONObject next=new JSONObject();
            jsonResponse.put("next",next);
            //报文签名信息，Json格式，机构开启签名验证时才有此项
            JSONObject secInfo=new JSONObject();
            secInfo.put("sign","");
            secInfo.put("signTyp","");
            jsonResponse.put("secInfo",secInfo);
        } catch (JSONException e) {
            logger.info(Utils.getStackTrace(e));
            throw new RuntimeException("openApi返回报文异常");
        }
        return jsonResponse.toString();
    }

}
