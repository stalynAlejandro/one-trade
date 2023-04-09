package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.TestUtils;
import com.pagonxt.onetradefinance.external.backend.api.model.CommentDto;
import com.pagonxt.onetradefinance.external.backend.api.model.CommentList;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CommentDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.CaseDataService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.integrations.model.Comment;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(CaseDataController.class)
@WithMockUser(username = "user", password = "user", authorities = { "ROLE_USER" })
class CaseDataControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/case-data";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserInfoService userInfoService;
    @MockBean
    CaseDataService caseDataService;
    @MockBean
    CommentDtoSerializer serializer;

    @Test
    void getComments_ok_returnsOk() throws Exception {
        // Given, when and then
        mockMvc.perform(get(URL_TEMPLATE + "/CLE-TEST/comments")
                        .param("locale", "es_es")
                        .with(httpBasic("user", "user")))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getComments_noParam_returnsBadRequest() throws Exception {
        // Given, when and then
        mockMvc.perform(get(URL_TEMPLATE + "/CLE-TEST/comments")
                        .with(httpBasic("user", "user")))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithAnonymousUser
    void getComments_noAuth_returnsUnauthorized() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE + "/CLE-TEST/comments")
                .param("locale", "es_es"))
                .andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    void getComments_ok_returnsValidData() throws Exception {
        // Given
        Comment comment = mapper.readValue(new ClassPathResource("data/model/comment.json").getFile(), Comment.class);
        CommentDto expectedCommentDto = mapper.readValue(new ClassPathResource("data/dto/comment-dto.json").getFile(), CommentDto.class);
        List<Comment> commentList = List.of(comment);
        CommentList expectedResult = new CommentList(List.of(expectedCommentDto));
        UserInfo userInfo = new UserInfo();
        when(userInfoService.getUserInfo("es_es")).thenReturn(userInfo);
        when(caseDataService.getComments("CLE-TEST", userInfo)).thenReturn(commentList);
        when(serializer.toDto(comment)).thenReturn(expectedCommentDto);
        // When
        MvcResult mvcResult =
                mockMvc.perform(get(URL_TEMPLATE + "/CLE-TEST/comments")
                        .param("locale", "es_es")
                        .with(httpBasic("user", "user")))
                        .andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertEquals(expectedJsonNode, resultJsonNode.get("entity"));
    }

    @Test
    void getComments_logDebug_returnsOk() throws NoSuchFieldException, IllegalAccessException {
        // Given
        CaseDataController caseDataController = new CaseDataController(userInfoService, caseDataService, serializer);
        Logger LOG = mock(Logger.class);
        TestUtils.setFinalStatic(CaseDataController.class.getDeclaredField("LOG"), LOG);
        when(LOG.isDebugEnabled()).thenReturn(true);
        // When
        caseDataController.getComments("requestIdTest", "localeTest");
        // Then
        verify(LOG).debug(anyString(), eq("requestIdTest"), eq("localeTest"), any());
    }
}
