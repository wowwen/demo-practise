package com.demo.practise.validator;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @FileName: UserControllerTest
 * @Author: owen
 * @Date: 2020-9-25 16:12
 * @Description: TODO
 */
@SpringBootTest
@WebAppConfiguration
@Slf4j
public class UserControllerTest {

    @BeforeEach
    public void init() {
        log.info("=====start======");
    }

    @AfterEach
    public void after() {
        log.info("=====finished======");
    }

    @Autowired
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testSaveUserControllerValidator() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setIdCardNo("430611200611025052");
        userDTO.setMobile("123");
        userDTO.setUserName("12345678901234567890123456789_");
        userDTO.setEmail("1@QQ.com");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(userDTO))).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testUpdateUserControllerValidator() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setIdCardNo("430611200611025052");
        userDTO.setMobile("123");
        userDTO.setUserName("12345678901234567890123456789_");
        userDTO.setEmail("1@QQ.com");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/update/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(userDTO))).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        log.info(mvcResult.getResponse().getContentAsString());
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