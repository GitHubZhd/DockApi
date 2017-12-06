package com.pengshu.crawler.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by ps on 2017/7/21.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class MockHttpTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();//建议使用这种
    }

    @Test
    public void Test4() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/asyn/openApi/test001/asyn/1/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"basicInfo\": {\"orgCode\": \"02001\",\"tranCode\": \"\",\"encTyp\": \"\",\"cTS\": 1232132322,\"cbUrl\": [\"http://192.168.1.82:8080/test/asyn1\"]},\"bizData\": \"112323\",\"secInfo\": {\"sign\": \"RSAPASS\",\"signTyp\": \"1\"}}")
                .param("file","index.html")
                .header("cache-control","no-cache")
                .header("accept","*/*")
                .header("host","localhost:8080")
                .header("accept-encoding","gzip, deflate")
                .header("connection","keep-alive")
                .header("async","true"))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult("我已收到请求"))
                .andReturn();


        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("我已收到请求"))
                .andDo(print());

    }
}

