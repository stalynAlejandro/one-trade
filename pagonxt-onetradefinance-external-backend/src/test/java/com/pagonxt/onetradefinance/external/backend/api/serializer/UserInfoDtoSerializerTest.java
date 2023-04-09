package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.UserInfoDto;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UnitTest
class UserInfoDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private
    UserInfoDtoSerializer serializer;

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        UserInfo userInfo = mapper.readValue(new ClassPathResource("data/model/user-info.json").getFile(), UserInfo.class);
        UserInfoDto expectedUserInfoDto = mapper.readValue(new ClassPathResource("data/dto/user-info-dto.json").getFile(), UserInfoDto.class);
        // When
        UserInfoDto result = serializer.toDto(userInfo);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedUserInfoDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNullUser_returnsNullFields() throws Exception {
        // Given
        UserInfo userInfo = mapper.readValue(new ClassPathResource("data/model/user-info.json").getFile(), UserInfo.class);
        userInfo.setUser(null);
        // When
        UserInfoDto result = serializer.toDto(userInfo);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        assertTrue(resultJsonNode.get("userId").isNull());
        assertTrue(resultJsonNode.get("userDisplayedName").isNull());
        assertTrue(resultJsonNode.get("userType").isNull());
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        UserInfoDto result = serializer.toDto(null);
        // Then
        assertNull(result);
    }

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        UserInfo expectedUserInfo = mapper.readValue(new ClassPathResource("data/model/user-info.json").getFile(), UserInfo.class);
        UserInfoDto userInfoDto = mapper.readValue(new ClassPathResource("data/dto/user-info-dto.json").getFile(), UserInfoDto.class);
        // When
        UserInfo result = serializer.toModel(userInfoDto);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedUserInfo);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        // When
        UserInfo result = serializer.toModel(null);
        // Then
        assertNull(result);
    }
}
