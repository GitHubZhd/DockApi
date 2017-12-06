package com.pengshu.crawler.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ps on 2017/8/11.
 */
public interface HandleService {

    public Object getHandle(HttpServletRequest request, HttpServletResponse response ,String url) throws IOException;

    public Object otherHandle(HttpServletRequest request, HttpServletResponse response ,String url) throws IOException;
}
