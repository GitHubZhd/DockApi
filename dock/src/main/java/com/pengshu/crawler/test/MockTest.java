package com.pengshu.crawler.test;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by ps on 2017/7/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MockTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();//建议使用这种
    }
    @Test
    public void test1() throws Exception {



//        mvc.perform(MockMvcRequestBuilders.post("/pass/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"loginName\":\"dhb\",\"pwd\":\"123\"}")
//                .header("cache-control","no-cache")
//                .header("accept","*/*")
//                .header("host","localhost:8080")
//                .header("accept-encoding","gzip, deflate")
//                .header("connection","keep-alive")
//                //.param("loginName", "dhb").param("pwd", "123")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("")));

//        mvc.perform(MockMvcRequestBuilders.post("/pass/auth/login/key")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"account\":\"TEST\",\"key\":\"20170531190458033089fd49a188d9cc\"}")
//                .header("cache-control","no-cache")
//                .header("accept","*/*")
//                .header("host","localhost:8080")
//                .header("accept-encoding","gzip, deflate")
//                .header("connection","keep-alive")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("success")));

        mvc.perform(MockMvcRequestBuilders.get("/pass/app/file/file_version")
                .contentType(MediaType.APPLICATION_JSON)
                .param("file","index.html")
                .header("cache-control","no-cache")
                .header("accept","*/*")
                .header("host","localhost:8080")
                .header("accept-encoding","gzip, deflate")
                .header("connection","keep-alive")
                .header("Authorization","3b9b44dc0c8a4cc895b23aa14ccbf35d")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("success")));


    }
}
