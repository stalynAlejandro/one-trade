package com.pagonxt.onetradefinance.work.api.backend;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksList;
import com.pagonxt.onetradefinance.integrations.model.tasks.PagoNxtTaskItem;
import com.pagonxt.onetradefinance.work.config.ControllerTest;
import com.pagonxt.onetradefinance.work.service.MyTasksService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(MyTasksController.class)
class MyTasksControllerTest {

    public static final String URL_TEMPLATE = "/backend/my-tasks";

    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.USE_LONG_FOR_INTS, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    @MockBean
    private MyTasksService myTasksService;


    @Test
    void getMyTasks_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getMyTasks_ok_returnsValidData() throws Exception {
        // Given
        MyTasksList expectedResult = new MyTasksList(List.of(getPagoNxtTaskItem()), 15L);
        when(myTasksService.getMyTasks(any())).thenReturn(expectedResult);

        // When
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        verify(myTasksService, times(1)).getMyTasks(any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    private PagoNxtTaskItem getPagoNxtTaskItem() {
        PagoNxtTaskItem result = new PagoNxtTaskItem();
        result.setOperationId("operationId");
        result.setMercuryRef("mercuryRef");
        result.setIssuanceDate(new Date());
        result.setProduct("product");
        result.setEvent("event");
        result.setClient("client");
        result.setTask("task");
        result.setPriority("priority");
        result.setStatus("status");
        result.setRequesterName("requesterName");
        return result;
    }
}
