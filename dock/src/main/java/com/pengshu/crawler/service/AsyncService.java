package com.pengshu.crawler.service;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Created by ps on 2017/7/24.
 */
public interface AsyncService {

    public Future<Object> asynHandle(HttpUriRequest request) throws IOException, JSONException;

    public HttpUriRequest isAsyn(HttpServletRequest request, String url);

}
