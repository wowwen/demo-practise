package com.example.demo.practise.provider1.validator;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.practise.DemoPractiseProvider1Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static sun.net.NetProperties.get;


/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: UserControllerTest2
 * @Author: jiangyw8
 * @Date: 2020-9-28 10:24
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
public class UserControllerTest2 {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testCreateUserControllerValidator() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setIdCardNo("46010819861112866X");
        userDTO.setMobile("17092110890");
        userDTO.setUserName("12345678901234567890");
        userDTO.setEmail("1@QQ.com");
        ScoresDTO scoresDTO = new ScoresDTO();
        scoresDTO.setMathScore(100.5);
        userDTO.setScoresDTO(scoresDTO);
        MemberDTO memberDTO = new MemberDTO();
        List list = new ArrayList();
        list.add(memberDTO);
        userDTO.setMembers(list);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/create/groups/nest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(userDTO))).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        log.info(mvcResult.getResponse().getContentAsString());
    }

}
