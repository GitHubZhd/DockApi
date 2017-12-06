package com.pengshu.crawler.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ps on 2017/7/20.
 */
@WebFilter(filterName = "trsHandleFilter" ,urlPatterns = "/openApi/*" ,asyncSupported = true)//开启异步处理支持
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class TrsHandleFilter implements Filter {

    private static final Logger logger= LoggerFactory.getLogger(TrsHandleFilter.class);

    @Autowired
    private Environment environment;

    @Value("${openApi.productCode}")
    private String productCode;

    @Value("${openAPi.productCode.apiName}")
    private String apiName;

    @Value("${openAPi.productCode.apiName.apiVer}")
    private String apiVer;

    @Value("${openAPi.productCode.apiName.apiVer.msgCode}")
    private String msgCode;

    private static String[] productCodeArray;
    private static String[] apiNameArray;
    private static String[] apiVerArray;
    private static String[] msgCodeArray;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        productCodeArray=productCode.split(",");
        apiNameArray=apiName.split(",");
        apiVerArray=apiVer.split(",");
        msgCodeArray=msgCode.split(",");

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest= (HttpServletRequest) servletRequest;
        MAPIHttpServletRequestWrapper mapiHttpServletRequestWrapper=new MAPIHttpServletRequestWrapper(httpServletRequest);

        ///openApi/{productCode}/{apiName}/{apiVer}/{msgCode}
        String uri=mapiHttpServletRequestWrapper.getRequestURI();
        logger.info("-----------------------------------"+uri);

        Map map=paraMap(mapiHttpServletRequestWrapper);
        mapiHttpServletRequestWrapper.setAttribute("host",map.get("host"));
        mapiHttpServletRequestWrapper.setAttribute("uri",map.get("uri"));

        filterChain.doFilter(mapiHttpServletRequestWrapper,servletResponse);

    }

    @Override
    public void destroy() {

    }


    public Map paraMap(HttpServletRequest request){

        String uri=request.getRequestURI();
        String temp="";
        boolean isExist=false;
        Map map=new HashMap();
        for (String p:productCodeArray){
            if(uri.startsWith(p)){
                temp=p;
                break;
            }
        }

        if(StringUtils.isEmpty(temp)){
            map.put("host","");
            map.put("uri","");
            return map;
        }

        for (String p:apiNameArray){
            if(uri.startsWith(p)){
                isExist=true;
                temp=p;
                break;
            }
        }
        if(!isExist){
            map.put("host",environment.getProperty(temp));
            map.put("uri",uri.replaceFirst(temp,"/"));
            return map;
        }

        isExist=false;
        for (String p:apiVerArray){
            if(uri.startsWith(p)){
                isExist=true;
                temp=p;
                break;
            }
        }
        if(!isExist){
            map.put("host",environment.getProperty(temp));
            map.put("uri",uri.replaceFirst(temp,"/"));
            return map;
        }

        for (String p:msgCodeArray){
            if(uri.startsWith(p)){
                temp=p;
                break;
            }
        }

        map.put("host",environment.getProperty(temp));
        map.put("uri",uri.replaceFirst(temp,"/"));

        return map;
    }
}
