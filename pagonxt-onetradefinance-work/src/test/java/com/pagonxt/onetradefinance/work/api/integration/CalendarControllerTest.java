package com.pagonxt.onetradefinance.work.api.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.service.CalendarService;
import com.pagonxt.onetradefinance.work.api.model.NextWorkDayResponse;
import com.pagonxt.onetradefinance.work.config.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(CalendarController.class)
class CalendarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CalendarService calendarService;

    @Test
    void getNextWorkDay_ok_returnsNextWorkDay() throws Exception {
        //given
        LocalDate now = LocalDate.now();
        doReturn(now).when(calendarService).nextWorkDay();

        //when and then
        MvcResult mvcResult = mockMvc.perform(get("/integrations/directory/calendar/next"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode resultJsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = objectMapper.valueToTree(new NextWorkDayResponse(now));
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }
}
