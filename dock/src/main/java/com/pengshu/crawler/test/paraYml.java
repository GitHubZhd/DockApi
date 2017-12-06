package com.pengshu.crawler.test;

import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ps on 2017/8/15.
 */
public class paraYml {

    public static void main(String[] args) {

        String s=String.format("%1$s_craw_%2$s_%3$s_%4$s","中国", "美国", "英国", "德国");
        System.out.println(s);


        Calendar calendar= GregorianCalendar.getInstance();
        calendar.add(Calendar.MINUTE,2);
        calendar.add(Calendar.SECOND,0);

        long ss=calendar.getTimeInMillis();
        long s1=(new Date()).getTime();
        System.out.println(ss);
        System.out.println(s1);

        long d=(ss-s1)/1000;
        System.out.println(d);

    }


    /**
     * 将普通文本转换成Base64编码的文本
     * @param value
     * @param charset
     * @return
     */
    public static String StringtoBase64String(String value, Charset charset){

        return Base64.encodeBase64String(value.getBytes(charset));

    }

    /**
     * 将Base64编码的文本转换成普通文本
     * @param value
     * @return
     */
    public static String Base64StringToString(String value){

        byte[] str=Base64.decodeBase64(value);

        return new String(str);
    }
}
