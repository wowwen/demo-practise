package com.demo.annotation.test;

import com.demo.annotation.springDI.primary.MessageController;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

//@RunWith(SpringRunner.class)
@SpringBootTest
class MessageControllerTest {

    @Autowired
    private MessageController messageController;

    private MockMvc mockMvc;

//    @Before  Junit4使用此注解
    @BeforeEach  //Junit5 换成了此注解
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    public void testSMS() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sms"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("sms content"));
    }
}
