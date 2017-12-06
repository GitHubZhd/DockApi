package com.pengshu.crawler.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by ps on 2017/7/21.
 */
public class Utils {

    /**
     * 日期转格式化为字符串
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date,String pattern){

        if(StringUtils.isEmpty(pattern)){
            pattern="yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleFormatter=new SimpleDateFormat(pattern);
        if(date==null){
            return simpleFormatter.format(new Date());
        }
        return simpleFormatter.format(date);
    }

    /**
     * inputStream转化为字符串
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String InputStreamToString(InputStream inputStream) throws IOException {
        if(inputStream==null){
            return "";
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String temp = "";
        StringBuilder tempResult=new StringBuilder();
        while ((temp = br.readLine()) != null) {
            String str = new String(temp.getBytes(), StandardCharsets.UTF_8);
            tempResult.append(str).append("\r\n");
        }
        return tempResult.toString();
    }

    /**
     * inputStream转换为字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] InputStreamToByte(InputStream inputStream) throws IOException {
        if(inputStream==null){
            return new byte[]{};
        }
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] dataByteArr = swapStream.toByteArray();
        return dataByteArr;

    }

    /**
     * 获取透传数据
     * @param message
     * @return
     */
    public static String paraMessage(String message){

        JSONObject jsStr;
        String bizData = null;
        try {
            jsStr = new JSONObject(message);
            Object object = jsStr.get("bizData");

            if(object instanceof  JSONObject){
                JSONObject temp= (JSONObject) object;
                bizData=temp.toString();
            }else if(object instanceof JSONArray){
                JSONArray temp=(JSONArray)object;
                bizData=temp.toString();
            }
        }catch (JSONException e){
            throw new RuntimeException("JSON解析出错"+e.getMessage());
        }
        return bizData==null?"":bizData.toString();
    }

    /**
     * 获取回调URL
     * @param message
     * @return
     */
    public static JSONArray paraCbUrl(String message) throws JSONException {
        if(StringUtils.isEmpty(message)){
            return new JSONArray();
        }
        JSONObject jsStr = new JSONObject(message);
        JSONObject basicInfo = (JSONObject) jsStr.get("basicInfo");
        JSONArray cbUrl= (JSONArray) basicInfo.get("cbUrl");

        return cbUrl==null?new JSONArray():cbUrl;
    }

    /**
     * 获取异常栈信息
     * @param e
     * @return
     */
    public static String getStackTrace(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer, true));
        return writer.toString();
    }

    public static boolean isWindowOS() {
        boolean isWindowOS = false;
        String osName = System.getProperty("os.name");
        if(osName.toLowerCase().indexOf("windows") > -1) {
            isWindowOS = true;
        }
        return isWindowOS;
    }

    public static InetAddress getInetAddress() {
        InetAddress inetAddress = null;
        try {
            //如果是windows操作系统
            if (isWindowOS()) {
                inetAddress = InetAddress.getLocalHost();
            } else {
                boolean bFindIP = false;
                //定义一个内容都是NetworkInterface的枚举对象
                Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>)
                        NetworkInterface.getNetworkInterfaces();
                //如果枚举对象里面还有内容(NetworkInterface)
                while (netInterfaces.hasMoreElements()) {
                    if (bFindIP) {
                        break;
                    }
                    //获取下一个内容(NetworkInterface)
                    NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                    //----------特定情况，可以考虑用ni.getName判断
                    //遍历所有IP
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    while (ips.hasMoreElements()) {
                        inetAddress = (InetAddress) ips.nextElement();
                        if (inetAddress.isSiteLocalAddress()         //属于本地地址
                                && !inetAddress.isLoopbackAddress()  //不是回环地址
                                && inetAddress.getHostAddress().indexOf(":") == -1) {   //地址里面没有:号
                            bFindIP = true;     //找到了地址
                            break;              //退出循环
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return inetAddress;
    }

    /**
     * 获取本机IP地址
     * @return
     */
    public static String getLocalIP() {
        return getInetAddress().getHostAddress();
    }
}
