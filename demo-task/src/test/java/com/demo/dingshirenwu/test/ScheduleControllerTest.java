package com.demo.dingshirenwu.test;

import com.demo.dingshirenwu.dynamic.update.ScheduleController;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
public class ScheduleControllerTest {

    @Autowired
    private ScheduleController scheduleController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();
    }

    @Test
    public void testChangeTaskCron() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/task/updateCron").param("cron", "0/20 * * * * ?"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * 这么调用，虽然cron发送过去了，但是不生效，task那边的周期还是没改
     */

}
